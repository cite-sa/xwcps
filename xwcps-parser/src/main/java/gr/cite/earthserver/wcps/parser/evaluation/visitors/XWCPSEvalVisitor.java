package gr.cite.earthserver.wcps.parser.evaluation.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathFactoryConfigurationException;

import gr.cite.commons.utils.xml.XMLConverter;
import gr.cite.commons.utils.xml.XPathEvaluator;
import gr.cite.commons.utils.xml.exceptions.XMLConversionException;
import gr.cite.commons.utils.xml.exceptions.XPathEvaluationException;
import gr.cite.earthserver.wcps.parser.evaluation.ForClauseInfo;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.wcps.grammar.XWCPSParser.AllCoveragesInServerLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.AllCoveragesLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.AttributeContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.BooleanXpathClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.CloseXmlElementContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.EndpointIdentifierContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.ForClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.IdentifierContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.LetClauseContext;
//import gr.cite.earthserver.wcps.grammar.XWCPSParser.MetadataClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.MetadataExpressionContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.MixedClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.OpenXmlElementContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.OpenXmlWithCloseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.OrderByClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.ProcessingExpressionContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.QuatedContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.SpecificIdInServerLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.SpecificIdLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.WhereClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.WrapResultClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.WrapResultSubElementContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XmlClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XmlClauseWithQuateContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XmlElementContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XpathClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XpathContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XpathForClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XwcpsContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.*;
import gr.cite.earthserver.wcps.parser.core.XwcpsReturnValue;
import gr.cite.earthserver.wcps.parser.evaluation.ForClauseInfo;
import gr.cite.earthserver.wcps.parser.evaluation.ForClauseInfo.ForClauseType;
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcps.parser.evaluation.Scope;
import gr.cite.earthserver.wcps.parser.evaluation.XpathForClause;
import gr.cite.earthserver.wcps.parser.utils.RankDefinition;
import gr.cite.earthserver.wcps.parser.utils.RankDefinition.OrderDirection;
import gr.cite.earthserver.wcps.parser.utils.RankingVector;
import gr.cite.earthserver.wcps.parser.utils.XWCPSEvalUtils;
import gr.cite.earthserver.wcs.adapter.api.WCSAdapterAPI;
import gr.cite.earthserver.wcs.core.Coverage;
import gr.cite.femme.client.FemmeClientException;
import gr.cite.femme.client.FemmeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.xpath.XPathFactoryConfigurationException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//import gr.cite.earthserver.wcps.grammar.XWCPSParser.MetadataClauseContext;

public class XWCPSEvalVisitor extends WCPSEvalVisitor {

    private static final Logger logger = LoggerFactory.getLogger(XWCPSEvalVisitor.class);

    private String forClauseDefaultXpath = null;

    private Stack<Scope> scopes = new Stack<>();

    public XWCPSEvalVisitor(WCSAdapterAPI wcsAdapter) {
        super(wcsAdapter);
    }

    @Override
    public Query visitXwcps(XwcpsContext ctx) {

        scopes.push(Scope.newRootScope());

        return visitChildren(ctx);
    }

    @Override
    public Query visitXpath(XpathContext ctx) {
        Query query = super.visitXpath(ctx);

        return query;
    }

    @Override
    public Query visitOrderByClause(OrderByClauseContext ctx) {
        OrderDirection direction = ctx.ASC() != null ? OrderDirection.Ascending : ctx.DESC() == null ? OrderDirection.Ascending : OrderDirection.Descending;

        Query rankingClause = null;
        if (ctx.xpathClause() != null) {
            rankingClause =  visit(ctx.xpathClause());
        }
        else  if (ctx.identifier() != null) {
            rankingClause =  visit(ctx.identifier());
        }

        List<RankDefinition> rankings = new ArrayList<>();
        RankDefinition ranking = new RankDefinition(direction);
        ranking.getRankingQueries().add(rankingClause);
        rankings.add(ranking);

        List<Coverage> ordered = this.sort(rankings);

        Query query = new Query();

        query.getOrderedCoverages().clear();
        query.getOrderedCoverages().addAll(ordered);

        query.getCoverageValueMap().clear();

        return query;
    }

    @Override
    public Query visitXpathForClause(XpathForClauseContext ctx) {
        Query q = visit(ctx.coverageVariableName());

        XpathForClauseEvalVisitor xpathForClauseEvalVisitor = new XpathForClauseEvalVisitor(this.getWcsAdapter());
        XpathForClause xpathForClause = xpathForClauseEvalVisitor.visit(ctx);

        List<Coverage> coverageList = xpathForClause.getCoverages();
        variables.put(ctx.coverageVariableName().getText(), coverageList);

        String coverages = coverageList.stream().map(Coverage::getCoverageId)
                .collect(Collectors.joining(", ", "( ", " )"));

        if (xpathForClause.getXpathQuery() != null) {
            forClauseDefaultXpath = xpathForClause.getXpathQuery();
        }

        q.setSplittedQuery(XWCPSEvalUtils.constructForQueries(ctx.coverageVariableName().getText(), coverageList));

        return q.appendQuery(" in " + coverages);
    }

    @Override
    public Query visitXpathClause(XpathClauseContext ctx) {

        // Query wcpsQuery = visit(ctx.scalarExpression());

        Query wcpsQuery = null;
        if (ctx.scalarExpression() != null) {
            wcpsQuery = visit(ctx.scalarExpression());
        } else if (ctx.metadataExpression() != null) {
            wcpsQuery = visit(ctx.metadataExpression());
        }

        // if there is no xquery in the query
        if (ctx.xpath() == null && forClauseDefaultXpath == null) {

            if (ctx.scalarExpression() != null && ctx.scalarExpression().getComponentExpression() != null) {
                return wcpsQuery;
            } else if (ctx.metadataExpression() != null) {
                return wcpsQuery;
            } else {
                /*
                 * evaluate wcps scalar expressions, ie for c in (AvgLandTemp)
				 * return <a attr=min(c[Lat(53.08), Long(8.80),
				 * ansi(\"2014-01\":\"2014-12\")]) > describeCoverage(c) </a>
				 */
                // try {

                String defaultForWhereClauseQuery = "for c in (NIR) return "; // FIXME
                if (this.forWhereClauseQuery != null) {
                    defaultForWhereClauseQuery = this.forWhereClauseQuery.getQuery() + " return ";
                }

                // String rewrittedQuery = defaultForWhereClauseQuery +
                // wcpsQuery.getQuery();

                if (ctx.getParent() instanceof ProcessingExpressionContext) {
                    wcpsQuery.simpleWCPS();
                }

                // TODO: add processCoverages to wcs adapter.
                // wcpsQuery.evaluated().setValue(
                // wcsRequestBuilder.processCoverages().query(rewrittedQuery).build().get().getResponse();

                return wcpsQuery;
                // } catch (WCSRequestException e) {
                // logger.error(e.getMessage(), e);
                //
                // throw new ParseCancellationException(e);
                // }
            }

        } else {

            Query xpathQuery = new Query();
            if (forClauseDefaultXpath != null) {
                xpathQuery.setQuery(forClauseDefaultXpath);
            }
            if (ctx.xpath() != null) {
                xpathQuery.aggregate(visit(ctx.xpath()));
            }

			/*
			 * rewrite to support xpath functions, eg
			 * min(describeCoverage(c)//@someValue) into
			 * describeCoverage(c)min(//@someValue)
			 */
            if (ctx.functionName() != null) {

                wcpsQuery.setValue(wcpsQuery.serializeValue());
                wcpsQuery.getCoverageValueMap().clear();
                // wcpsQuery.setCoverageValueMap(null);
                XWCPSEvalUtils.wrapDefaultXmlReturnElement(wcpsQuery);

                xpathQuery.prependQuery(ctx.functionName().getText() + "(").appendQuery(")");
            }

            try {
                return evaluateXpath(wcpsQuery, xpathQuery);
            } catch (XPathFactoryConfigurationException e) {
                logger.error(e.getMessage(), e);

                throw new ParseCancellationException(e);
            }
        }
    }

    @Override
    public Query visitLetClause(LetClauseContext ctx) {
        Query letClauseQuery = this.visit(ctx.letClauseExpression());

        Set<String> variables = scopes.peek().getVariables();
        String variable = ctx.identifier().getText();

        scopes.peek().setVariable(variable, letClauseQuery);

//        String value = letClause.serializeValue();
//
//        scopes.peek().setVariable(variable, value);

        return letClauseQuery;
    }

//    public Query visitLetClauseExpression(LetClauseExpressionContext ctx) {
//        if (ctx.processingExpression() != null) return this.visit(ctx.processingExpression());
//        return this.visit(ctx.arithmeticExpression());
//    }

    public Query visitArithmeticExpression(ArithmeticExpressionContext ctx) {
        if (ctx.coverageExpression() != null) {
            if (ctx.coverageArithmeticOperator() != null) {
                Query leftArithmeticExpression = this.visit(ctx.coverageExpression(0));
                Query rightArithmeticExpression = this.visit(ctx.coverageExpression(1));

                if (leftArithmeticExpression.getCoverageValueMap().size() > 0 && rightArithmeticExpression.getCoverageValueMap().size() > 0) {
                    if (ctx.coverageArithmeticOperator().PLUS() != null) {

                    }
                }
            }
        }

        return super.visitArithmeticExpression(ctx);
    }

    @Override
    public Query visitXmlElement(XmlElementContext ctx) {
        Query query = visit(ctx.LOWER_THAN()).setValue(ctx.LOWER_THAN().getText());

        query.aggregate(visit(ctx.qName()).setValue(ctx.qName().getText()));

        Query attributes = new Query();
        for (AttributeContext attributeContext : ctx.attribute()) {
            Query attributeQuery = visit(attributeContext);
            attributes.aggregate(attributeQuery);
        }

        if (!attributes.getCoverageValueMap().isEmpty()) {

            String xmlElementString = query.getValue() + " ";

            for (Entry<Coverage, XwcpsReturnValue> entry : attributes.getCoverageValueMap().entrySet()) {
                String xmlPayload = entry.getValue().getXwcpsValue();
                Query localQuery = new Query().evaluated();

                Map<Coverage, XwcpsReturnValue> localCoverageValueMap = new HashMap<>();
                localCoverageValueMap.put(entry.getKey(), new XwcpsReturnValue(xmlElementString + xmlPayload));
                localQuery.getCoverageValueMap().putAll(localCoverageValueMap);
                // localQuery.setCoverageValueMap(localCoverageValueMap);

                query.aggregate(localQuery);
            }
        } else {
            if (attributes.getValue() != null) {
                query.appendValue(" ").aggregate(attributes);
            }
        }

        return query;
    }

    @Override
    public Query visitOpenXmlElement(OpenXmlElementContext ctx) {
        Query query = visit(ctx.xmlElement());

        query.aggregate(visit(ctx.GREATER_THAN()).setValue(ctx.GREATER_THAN().getText()));

        return query;
    }

    @Override
    public Query visitOpenXmlWithClose(OpenXmlWithCloseContext ctx) {
        Query query = visit(ctx.xmlElement());

        query.aggregate(visit(ctx.GREATER_THAN_SLASH()).setValue(ctx.GREATER_THAN_SLASH().getText()));

        return query;
    }

    @Override
    public Query visitCloseXmlElement(CloseXmlElementContext ctx) {
        return super.visitCloseXmlElement(ctx).setValue(ctx.getText());
    }

    @Override
    public Query visitQuated(QuatedContext ctx) {
        return super.visitQuated(ctx).setValue(XWCPSEvalUtils.removeQuates(ctx.getText()));
    }

    @Override
    public Query visitXmlClause(XmlClauseContext ctx) {

        if (!ctx.openXmlWithClose().isEmpty()) {
            return super.visitXmlClause(ctx);
        }

        Query openXmlElementQuery = visit(ctx.openXmlElement());
        Query closeXmlElementQuery = visit(ctx.closeXmlElement());

        Query payload = new Query();
        if (ctx.xmlPayload() != null) {
            payload.aggregate(visit(ctx.xmlPayload()));
        } else {

            if (ctx.quated() != null) {
                payload.aggregate(visit(ctx.quated()));
            }

            for (XmlClauseWithQuateContext context : ctx.xmlClauseWithQuate()) {
                payload.aggregate(visit(context));
            }
        }

        Query q = new Query();

        if (!payload.getCoverageValueMap().isEmpty()) {
            for (Entry<Coverage, XwcpsReturnValue> entry : payload.getCoverageValueMap().entrySet()) {
                String xmlPayload = entry.getValue().getXwcpsValue();
                if (xmlPayload == null || xmlPayload.isEmpty()) {
                    continue;
                }
                Query localQuery = new Query().evaluated();
                Map<Coverage, XwcpsReturnValue> localCoverageValueMap = new HashMap<>();
                localCoverageValueMap.put(entry.getKey(), new XwcpsReturnValue(
                        openXmlElementQuery.getValue() + xmlPayload + closeXmlElementQuery.getValue()));
                localQuery.getCoverageValueMap().putAll(localCoverageValueMap);
                // localQuery.setCoverageValueMap(localCoverageValueMap);
                q.aggregate(localQuery);
            }
        } else {
            payload.prependValue(openXmlElementQuery.getValue()).appendValue(closeXmlElementQuery.getValue());

            q.aggregate(payload);
        }

        return q;
    }

    @Override
    public Query visitAttribute(AttributeContext ctx) {
        Query attribute = visit(ctx.qName()).setValue(ctx.qName().getText());
        attribute.aggregate(visit(ctx.EQUAL()).setValue(ctx.EQUAL().getText()));

        Query q = new Query();

        if (ctx.quated() != null) {
            q.aggregate(attribute).aggregate(visit(ctx.quated()).setValue(ctx.quated().getText()));
        } else {

            Query payload = visit(ctx.xpathClause());
            if (!payload.getCoverageValueMap().isEmpty()) {
                for (Entry<Coverage, XwcpsReturnValue> entry : payload.getCoverageValueMap().entrySet()) {
                    String xmlPayload = entry.getValue().getXwcpsValue();
                    Query localQuery = new Query().evaluated();

                    Map<Coverage, XwcpsReturnValue> localCoverageValueMap = new HashMap<>();
                    localCoverageValueMap.put(entry.getKey(),
                            new XwcpsReturnValue(attribute.getValue() + "\"" + xmlPayload + "\""));
                    // localQuery.setCoverageValueMap(localCoverageValueMap);
                    localQuery.getCoverageValueMap().putAll(localCoverageValueMap);

                    q.aggregate(localQuery);
                }
            } else {
                q.setValue(attribute.getValue() + "\"");
                q.aggregate(payload);
                q.setValue(q.getValue() + "\"");
            }
        }

        return q;
    }

    @Override
    public Query visitProcessingExpression(ProcessingExpressionContext ctx) {
        if (ctx.encodedCoverageExpression() != null || ctx.wrapResultClause() != null
                || ctx.getParent() instanceof WrapResultClauseContext) {
            return super.visitProcessingExpression(ctx);
        }

        Query xwcpsQuery = super.visitProcessingExpression(ctx);
        xwcpsQuery.setValue(xwcpsQuery.serializeValue());

        return xwcpsQuery.isSimpleWCPS() ? xwcpsQuery : XWCPSEvalUtils.wrapDefaultXmlReturnElement(xwcpsQuery);

    }

    @Override
    public Query visitWrapResultClause(WrapResultClauseContext ctx) {

        Stack<String> openXmlElementNames = new Stack<>();

        Query wrapper = new Query();

        Query openXmlelement = visit(ctx.openXmlElement());
        openXmlElementNames.push(XWCPSEvalUtils.getElementName(ctx.openXmlElement()));

        wrapper.aggregate(openXmlelement);

        for (WrapResultSubElementContext subElementCtx : ctx.wrapResultSubElement()) {
            if (subElementCtx.getChild(0) instanceof OpenXmlElementContext) {
                openXmlElementNames.push(XWCPSEvalUtils.getElementName(subElementCtx.openXmlElement()));
            }

            Query subElement = visit(subElementCtx);

            wrapper.aggregate(subElement);
        }

        Query wrappedValue = visit(ctx.processingExpression());

        wrapper.aggregate(wrappedValue);

        // close all open elements
        do {
            wrapper.appendValue("</" + openXmlElementNames.pop() + ">");
        } while (!openXmlElementNames.isEmpty());

        // wrapper.setCoverageValueMap(null);
        wrapper.getCoverageValueMap().clear();
        return wrapper;
    }

    @Override
    public Query visitIdentifier(IdentifierContext ctx) {

        Set<String> variables = scopes.peek().getVariables();

        Query identifier = null;

        if (variables.contains(ctx.getText())) {
            identifier = scopes.peek().getVariableValue(ctx.getText());
        } else {
            //Query value = scopes.peek().getVariableValue(ctx.getText());
            identifier = super.visitIdentifier(ctx);

//            if (value != null) {
//                logger.info("variable " + ctx.getText() + " was already evaluated: " + ctx.getText() + " = " + value);
//                //identifier.setValue(value);
//                identifier = value;
//            }
        }

        return identifier;
    }

    @Override
    public Query visitBooleanXpathClause(BooleanXpathClauseContext ctx) {
//		Query xpathQuery = visit(ctx.xpathClause());
//
//		Map<Coverage, XwcpsReturnValue> filteredResult = xpathQuery.getCoverageValueMap().entrySet().stream()
//				.filter(e -> !e.getValue().getXwcpsValue().isEmpty())
//				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
//
//		xpathQuery.getCoverageValueMap().clear();
//		xpathQuery.getCoverageValueMap().putAll(filteredResult);
//
//		xpathQuery.setEvaluated(false);
//
//		xpathQuery.setQuery("");
//
//		return xpathQuery;

        Query xpathQuery = visit(ctx.xpathClause().xpath());
        return xpathQuery;
    }

    @Override
    public Query visitWhereClause(WhereClauseContext ctx) {
//		Query whereClause = super.visitWhereClause(ctx);
//		
//		if (whereClause.getQuery().trim().endsWith(ctx.WHERE().getText())) {
//			whereClause.setQuery("");
//			whereClause.setAggregatedValue("");
//		}
//
//		if (!whereClause.getCoverageValueMap().isEmpty()) {
//			for (String key : this.variables.keySet()) {
//				this.variables.get(key).retainAll(whereClause.getCoverageValueMap().keySet());
//			}
//		}
//
//		return whereClause;

        Query whereClause = null;

        if (ctx.booleanScalarExpression() != null) {
            whereClause = visit(ctx.booleanScalarExpression());

            if (whereClause.getQuery().trim().endsWith(ctx.WHERE().getText())) {
                whereClause.setQuery("");
                whereClause.setAggregatedValue("");
            }

            if (!whereClause.getCoverageValueMap().isEmpty()) {
                for (String key : this.variables.keySet()) {
                    this.variables.get(key).retainAll(whereClause.getCoverageValueMap().keySet());
                }
            }
        } else if (ctx.booleanXpathClause() != null) {
            whereClause = visit(ctx.booleanXpathClause());
            whereClause.setXpath(whereClause.getQuery());
        }

        return whereClause;
    }

    @Override
    public Query visitMixedClause(MixedClauseContext ctx) {
        Query encodedCoverageExpressionQuery = visit(ctx.encodedCoverageExpression());

        Query metadataQuery = null;
        if (ctx.xmlClause() != null) {
            metadataQuery = visit(ctx.xmlClause());
        } else {
            metadataQuery = visit(ctx.xpathClause());
        }

        Query result = new Query();

        if (encodedCoverageExpressionQuery != null) {
            result.getCoverageValueMap().putAll(encodedCoverageExpressionQuery.getCoverageValueMap());
        }

        if (metadataQuery != null) {
            for (Entry<Coverage, XwcpsReturnValue> coverageMetadataEntry : metadataQuery.getCoverageValueMap()
                    .entrySet()) {
                if (result.getCoverageValueMap().containsKey(coverageMetadataEntry.getKey())) {
                    result.getCoverageValueMap().get(coverageMetadataEntry.getKey())
                            .setXwcpsValue(coverageMetadataEntry.getValue().getXwcpsValue());
                } else {
                    result.getCoverageValueMap().put(coverageMetadataEntry.getKey(), coverageMetadataEntry.getValue());
                }
            }
        }

        result.evaluated();

        return result;
    }

    @Override
    public Query visitMetadataExpression(MetadataExpressionContext ctx) {
        Query query = super.visitMetadataExpression(ctx);

        String variable = ctx.coverageVariableName().getText();
        Map<Coverage, XwcpsReturnValue> metadataCoverages = variables.get(variable).stream().map(coverage -> {
            // TODO: Return all coverages, not just the first one
            // TODO: change it because we have already done the query to femme

            // List<Coverage> coverages =
            // this.getWcsAdapter().getCoveragesByCoverageId(coverage.getCoverageId());
            // String describeCoverage = "";
            // if (coverages.size() > 0) {
            // describeCoverage = coverages.get(0).getMetadata();
            //
            // result.setXwcpsValue("<coverage id='" + coverage.getCoverageId()
            // + "'>"
            // + describeCoverage.replace("<?xml version=\"1.0\"
            // encoding=\"UTF-8\"?>", "") + "</coverage>");
            // }

            XwcpsReturnValue result = new XwcpsReturnValue();

            String describeCoverage = coverage.getMetadata();
            result.setXwcpsValue("<coverage id='" + coverage.getCoverageId() + "'>"
                    + describeCoverage.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "") + "</coverage>");

            Entry<Coverage, XwcpsReturnValue> entry = new SimpleImmutableEntry<>(coverage, result);

            return entry;
        }).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        query.getCoverageValueMap().clear();
        query.getCoverageValueMap().putAll(metadataCoverages);

        return query.evaluated();
    }

    @Override
    public Query visitSpecificIdInServerLabel(SpecificIdInServerLabelContext ctx) {
        Query query = super.visitSpecificIdInServerLabel(ctx);

        String coverageId = ctx.identifier().getText();
        String endpoint = "";
        EndpointIdentifierContext endpointContext = ctx.endpointIdentifier();
        if (endpointContext.identifier() != null) {
            endpoint = endpointContext.identifier().getText();
        } else if (endpointContext.STRING_LITERAL() != null) {
            endpoint = endpointContext.STRING_LITERAL().getText();
            endpoint = endpoint.replace("\"", "");
        } else if (endpointContext.XPATH_LITERAL() != null) {
            endpoint = endpointContext.XPATH_LITERAL().getText();
        }

        // Coverage coverage = null;
        // try {
        // coverage =
        // this.getWcsAdapter().getCoverageByCoverageIdInServer(endpoint,
        // coverageId);
        // } catch (FemmeDatastoreException | FemmeClientException e) {
        // e.printStackTrace();
        // }
        // if (coverage == null) {
        // coverage = new Coverage();
        // coverage.setCoverageId(coverageId);
        // }

//        query.setForType(ForClauseType.SPECIFIC_ID_IN_SERVER);
//        query.setCoverageId(coverageId);
//        query.setEndpoint(endpoint);

        ForClauseInfo forClauseInfo = new ForClauseInfo();
        forClauseInfo.setForType(ForClauseType.SPECIFIC_ID_IN_SERVER);
        forClauseInfo.setCoverageId(coverageId);
        forClauseInfo.setEndpoint(endpoint);
        query.getForClauseInfo().add(forClauseInfo);

        return query.evaluated();
    }

    @Override
    public Query visitAllCoveragesInServerLabel(AllCoveragesInServerLabelContext ctx) {
        Query query = super.visitAllCoveragesInServerLabel(ctx);

        EndpointIdentifierContext endpointContext = ctx.endpointIdentifier();
        String endpoint = "";
        if (endpointContext.identifier() != null) {
            endpoint = endpointContext.identifier().getText();
        } else if (endpointContext.STRING_LITERAL() != null) {
            endpoint = endpointContext.STRING_LITERAL().getText();
            endpoint = endpoint.replace("\"", "");
        } else if (endpointContext.XPATH_LITERAL() != null) {
            endpoint = endpointContext.XPATH_LITERAL().getText();
        }

//        query.setForType(ForClauseType.ALL_COVERAGES_IN_SERVER);
//        query.setEndpoint(endpoint);

        ForClauseInfo forClauseInfo = new ForClauseInfo();
        forClauseInfo.setForType(ForClauseType.ALL_COVERAGES_IN_SERVER);
        forClauseInfo.setEndpoint(endpoint);
        query.getForClauseInfo().add(forClauseInfo);

        return query.evaluated();
    }

    @Override
    public Query visitAllCoveragesLabel(AllCoveragesLabelContext ctx) {
        Query query = super.visitAllCoveragesLabel(ctx);

//        query.setForType(ForClauseType.ALL_COVERAGES);

        ForClauseInfo forClauseInfo = new ForClauseInfo();
        forClauseInfo.setForType(ForClauseType.ALL_COVERAGES);
        query.getForClauseInfo().add(forClauseInfo);

        return query.evaluated();
    }

    @Override
    public Query visitSpecificIdLabel(SpecificIdLabelContext ctx) {
        Query query = super.visitSpecificIdLabel(ctx);

//        query.setForType(ForClauseType.SPECIFIC_ID);
//        query.setCoverageId(ctx.identifier().getText());

        ForClauseInfo forClauseInfo = new ForClauseInfo();
        forClauseInfo.setForType(ForClauseType.SPECIFIC_ID);
        forClauseInfo.setCoverageId(ctx.identifier().getText());
        query.getForClauseInfo().add(forClauseInfo);

        return query.evaluated();
    }

    @Override
    public Query visitForClause(ForClauseContext ctx) {
        Query query = super.visitForClause(ctx);

        List<Coverage> coverages = new ArrayList<>(query.getCoverageValueMap().keySet());

        query.setSplittedQuery(XWCPSEvalUtils.constructForQueries(ctx.coverageVariableName().getText(), coverages));

        this.variables.put(ctx.coverageVariableName().getText(), coverages);

        query.setVariableName(ctx.coverageVariableName().getText());

        return query;
    }

    private static Query evaluateXpath(Query wcpsQuery, Query xpathQuery) throws XPathFactoryConfigurationException {
        if (!wcpsQuery.getCoverageValueMap().isEmpty()) {
            Map<Coverage, XwcpsReturnValue> results = new HashMap<>();
            for (Entry<Coverage, XwcpsReturnValue> entry : wcpsQuery.getCoverageValueMap().entrySet()) {
                String xPathResult = XWCPSEvalVisitor.evaluateXpath(entry.getValue().getXwcpsValue(), xpathQuery.getQuery());
                if (!"".equals(xPathResult)) {

                    XwcpsReturnValue xwcpsReturnValue = new XwcpsReturnValue();
                    xwcpsReturnValue.setXwcpsValue(xPathResult);
                    xwcpsReturnValue.setWcpsMediaType(entry.getValue().getWcpsMediaType());
                    xwcpsReturnValue.setWcpsValue(entry.getValue().getWcpsValue());
                    xwcpsReturnValue.setSubQuery(entry.getValue().getSubQuery());

                    results.put(entry.getKey(), xwcpsReturnValue);
                }
            }
            wcpsQuery.getCoverageValueMap().clear();
            wcpsQuery.getCoverageValueMap().putAll(results);
        } else {
            String value = XWCPSEvalVisitor.evaluateXpath(wcpsQuery.getValue(), xpathQuery.getQuery());
            wcpsQuery.aggregate(xpathQuery.setValue(value), true);
        }

        return wcpsQuery;
    }

    private static String evaluateXpath(String node, String xpath) throws XPathFactoryConfigurationException {
		/*
		 * remove whitespaces between namespace and localname eg: // mynamespace
		 * : mylocalname [@ mynamespace2 : myattr = 1]
		 */

        Pattern pattern = Pattern.compile("[\\/\\@]\\s*\\w*(\\s)*:(\\s)*");
        Matcher matcher = pattern.matcher(xpath);
        while (matcher.find()) {
            String match = matcher.group();

            String replacement = match.replaceAll("\\s+", "");
            xpath = xpath.substring(0, matcher.start()) + replacement + xpath.substring(matcher.end());
        }

        XPathEvaluator xPathEvaluator = null;
        try {
            xPathEvaluator = new XPathEvaluator(XMLConverter.stringToNode(node, true), true, false);
        } catch (XMLConversionException e) {
            e.printStackTrace();
            XWCPSEvalVisitor.logger.info("XPathEvaluator error with message " + e.getMessage());
        }

        List<String> xpathResults = null;
        try {
            xpathResults = xPathEvaluator.evaluate(xpath);
        } catch (XPathEvaluationException e) {
            e.printStackTrace();
            XWCPSEvalVisitor.logger.info("XPathEvaluationException error with message " + e.getMessage());
        }

        return xpathResults == null ? "" : xpathResults.stream().collect(Collectors.joining(" "));
    }

    private List<Coverage> sort(List<RankDefinition> rankingDefinitions) {
        List<List<Object>> rankValues = new ArrayList<List<Object>>();

        List<Coverage> coverages = new ArrayList<>();
        for (RankDefinition rankDefinition : rankingDefinitions) {
            Query query = rankDefinition.getRankingQueries().get(0);

            List<Object> xpathValues = new ArrayList<>();
            for (Entry<Coverage, XwcpsReturnValue> entry : query.getCoverageValueMap().entrySet()) {
                String xmlPayload = entry.getValue().getXwcpsValue();
                xpathValues.add(xmlPayload);
            }

            rankValues.add(xpathValues);

            coverages.clear();
            coverages.addAll(query.getCoverageValueMap().keySet());
        }

        List<RankingVector> vectors = new ArrayList<RankingVector>();
        for (int i = 0; i < coverages.size(); i += 1) {
            RankingVector v = new RankingVector(coverages.get(i), rankingDefinitions);
            for (int z = 0; z < rankingDefinitions.size(); z += 1) {
                v.getValues()[z] = rankValues.get(z).get(i);
            }
            vectors.add(v);
        }

        Collections.sort(vectors);

        List<Coverage> sorted = vectors.stream().map(x -> x.getItem()).collect(Collectors.toList());

        return sorted;
    }
}
