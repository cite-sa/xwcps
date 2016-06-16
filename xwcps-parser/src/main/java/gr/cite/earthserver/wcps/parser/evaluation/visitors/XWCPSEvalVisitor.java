package gr.cite.earthserver.wcps.parser.evaluation.visitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathFactoryConfigurationException;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.AttributeContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.BooleanXpathClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.CloseXmlElementContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.IdentifierContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.LetClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.OpenXmlElementContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.OpenXmlWithCloseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.ProcessingExpressionContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.QuatedContext;
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
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcps.parser.evaluation.Scope;
import gr.cite.earthserver.wcps.parser.evaluation.XpathForClause;
import gr.cite.earthserver.wcps.parser.utils.XWCPSEvalUtils;
import gr.cite.earthserver.wcs.client.WCSRequestBuilder;
import gr.cite.earthserver.wcs.client.WCSRequestException;
import gr.cite.femme.query.criteria.CriteriaQuery;
import gr.cite.scarabaeus.utils.xml.XMLConverter;
import gr.cite.scarabaeus.utils.xml.XPathEvaluator;

public class XWCPSEvalVisitor extends WCPSEvalVisitor {
	private static final Logger logger = LoggerFactory.getLogger(XWCPSEvalVisitor.class);

	private Stack<Pair<Query, Query>> xmlReturnElementsStack = new Stack<>();

	private CriteriaQuery<Coverage> exmmsQuery;

	private String forClauseDefaultXpath = null;

	private Stack<Scope> scopes = new Stack<>();

	/**
	 * 
	 * @param wcsEndpoint
	 *            url on the federated rasdaman
	 * @param exmmsQuery
	 *            TODO
	 */
	public XWCPSEvalVisitor(String wcsEndpoint, CriteriaQuery<Coverage> exmmsQuery) {
		super(wcsEndpoint);
		this.exmmsQuery = exmmsQuery;
	}

	/**
	 * 
	 * @param wcsRequestBuilder
	 *            wcs request builder with url on the federated rasdaman
	 */
	public XWCPSEvalVisitor(WCSRequestBuilder wcsRequestBuilder, CriteriaQuery<Coverage> exmmsQuery) {
		super(wcsRequestBuilder);
		this.exmmsQuery = exmmsQuery;
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
	public Query visitXpathForClause(XpathForClauseContext ctx) {
		Query q = visit(ctx.coverageVariableName());

		XpathForClauseEvalVisitor xpathForClauseEvalVisitor = new XpathForClauseEvalVisitor(exmmsQuery);
		XpathForClause xpathForClause = xpathForClauseEvalVisitor.visit(ctx);

		List<Coverage> coverageList = xpathForClause.getCoverages();
		variables.put(ctx.coverageVariableName().getText(), coverageList);

		String coverages = coverageList.stream().map(Coverage::getLocalId)
				.collect(Collectors.joining(", ", "( ", " )"));

		if (xpathForClause.getXpathQuery() != null) {
			forClauseDefaultXpath = xpathForClause.getXpathQuery();
		}

		q.setSplittedQuery(XWCPSEvalUtils.constructForQueries(ctx.coverageVariableName().getText(), coverageList));

		return q.appendQuery(" in " + coverages);
	}

	@Override
	public Query visitXpathClause(XpathClauseContext ctx) {

		Query wcpsQuery = visit(ctx.scalarExpression());

		// if there is no xquery in the query
		if (ctx.xpath() == null && forClauseDefaultXpath == null) {

			if (ctx.scalarExpression().getComponentExpression() != null) {
				return wcpsQuery;
			} else {
				/*
				 * evaluate wcps scalar expressions, ie for c in (AvgLandTemp)
				 * return <a attr=min(c[Lat(53.08), Long(8.80),
				 * ansi(\"2014-01\":\"2014-12\")]) > describeCoverage(c) </a>
				 */
				try {

					String defaultForWhereClauseQuery = "for c in (NIR) return "; // FIXME
					if (this.forWhereClauseQuery != null) {
						defaultForWhereClauseQuery = this.forWhereClauseQuery.getQuery() + " return ";
					}

					String rewrittedQuery = defaultForWhereClauseQuery + wcpsQuery.getQuery();

					if (ctx.getParent() instanceof ProcessingExpressionContext) {
						wcpsQuery.simpleWCPS();
					}

					return wcpsQuery.evaluated().setValue(wcsRequestBuilder.processCoverages().query(rewrittedQuery)
							.build().get().getAggregatedValue());
				} catch (WCSRequestException e) {
					logger.error(e.getMessage(), e);

					throw new ParseCancellationException(e);
				}
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
				wcpsQuery.setCoverageValueMap(null);
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
		Query letClause = super.visitLetClause(ctx);

		String variable = ctx.identifier().getText();
		String value = letClause.serializeValue();

		scopes.peek().setVariable(variable, value);

		return letClause;
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

		if (attributes.getCoverageValueMap() != null) {

			String xmlElementString = query.getValue() + " ";

			for (Entry<Coverage, String> entry : attributes.getCoverageValueMap().entrySet()) {
				String xmlPayload = entry.getValue();
				Query localQuery = new Query().evaluated();

				Map<Coverage, String> localCoverageValueMap = new HashMap<>();
				localCoverageValueMap.put(entry.getKey(), xmlElementString + xmlPayload);
				localQuery.setCoverageValueMap(localCoverageValueMap);

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

		if (payload.getCoverageValueMap() != null) {
			for (Entry<Coverage, String> entry : payload.getCoverageValueMap().entrySet()) {
				String xmlPayload = entry.getValue();
				if (xmlPayload == null || xmlPayload.isEmpty()) {
					continue;
				}
				Query localQuery = new Query().evaluated();
				Map<Coverage, String> localCoverageValueMap = new HashMap<>();
				localCoverageValueMap.put(entry.getKey(),
						openXmlElementQuery.getValue() + xmlPayload + closeXmlElementQuery.getValue());
				localQuery.setCoverageValueMap(localCoverageValueMap);
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
			if (payload.getCoverageValueMap() != null) {
				for (Entry<Coverage, String> entry : payload.getCoverageValueMap().entrySet()) {
					String xmlPayload = entry.getValue();
					Query localQuery = new Query().evaluated();

					Map<Coverage, String> localCoverageValueMap = new HashMap<>();
					localCoverageValueMap.put(entry.getKey(), attribute.getValue() + "\"" + xmlPayload + "\"");
					localQuery.setCoverageValueMap(localCoverageValueMap);

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

		wrapper.setCoverageValueMap(null);
		return wrapper;
	}

	@Override
	public Query visitIdentifier(IdentifierContext ctx) {

		String value = scopes.peek().getVariableValue(ctx.getText());

		Query identifier = super.visitIdentifier(ctx);

		if (value != null) {
			logger.info("variable " + ctx.getText() + " was already evaluated: " + ctx.getText() + " = " + value);
			identifier.setValue(value);
		}

		return identifier;
	}

	private static Query evaluateXpath(Query wcpsQuery, Query xpathQuery) throws XPathFactoryConfigurationException {
		if (wcpsQuery.getCoverageValueMap() != null) {
			for (Entry<Coverage, String> entry : wcpsQuery.getCoverageValueMap().entrySet()) {
				entry.setValue(evaluateXpath(entry.getValue(), xpathQuery.getQuery()));
			}
		} else {
			String value = evaluateXpath(wcpsQuery.getValue(), xpathQuery.getQuery());
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

		XPathEvaluator xPathEvaluator = new XPathEvaluator(XMLConverter.stringToNode(node, true), true, false);
		List<String> xpathResults = xPathEvaluator.evaluate(xpath);

		return xpathResults == null ? "" : xpathResults.stream().collect(Collectors.joining(" "));
	}

	@Override
	public Query visitBooleanXpathClause(BooleanXpathClauseContext ctx) {
		Query xpathQuery = visit(ctx.xpathClause());

		xpathQuery.setCoverageValueMap(xpathQuery.getCoverageValueMap().entrySet().stream()
				.filter(e -> !e.getValue().isEmpty()).collect(Collectors.toMap(Entry::getKey, Entry::getValue)));

		xpathQuery.setEvaluated(false);

		xpathQuery.setQuery("");

		return xpathQuery;
	}

	@Override
	public Query visitWhereClause(WhereClauseContext ctx) {
		Query whereClause = super.visitWhereClause(ctx);

		if (whereClause.getQuery().trim().endsWith(ctx.WHERE().getText())) {
			whereClause.setQuery("");
			whereClause.setAggregatedValue("");
		}

		// clear coverage values
		for (Entry<Coverage, String> coverageEntry : whereClause.getCoverageValueMap().entrySet()) {
			coverageEntry.setValue(null);
		}

		return whereClause;
	}

}
