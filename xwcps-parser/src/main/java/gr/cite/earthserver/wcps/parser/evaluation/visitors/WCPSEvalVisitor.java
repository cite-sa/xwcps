package gr.cite.earthserver.wcps.parser.evaluation.visitors;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import gr.cite.earthserver.wcps.parser.evaluation.ForClauseInfo;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import gr.cite.earthserver.wcps.grammar.XWCPSParser.DescribeCoverageExpressionLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.EncodedCoverageExpressionLabelContext;
//import gr.cite.earthserver.wcps.grammar.XWCPSParser.ForClauseLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.WcpsQueryContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XwcpsContext;
import gr.cite.earthserver.wcps.parser.core.XwcpsQueryResult;
import gr.cite.earthserver.wcps.parser.core.XwcpsReturnValue;
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcps.parser.utils.XWCPSEvalUtils;
import gr.cite.earthserver.wcs.adapter.api.WCSAdapterAPI;
import gr.cite.earthserver.wcs.core.Coverage;
import gr.cite.earthserver.wcs.core.WCSRequestBuilder;
import gr.cite.earthserver.wcs.core.WCSRequestException;
import gr.cite.earthserver.wcs.core.WCSResponse;
import gr.cite.femme.client.FemmeClientException;
import gr.cite.femme.client.FemmeDatastoreException;
import gr.cite.femme.query.api.QueryOptionsFields;

public abstract class WCPSEvalVisitor extends XWCPSParseTreeVisitor {
    private static final Logger logger = LoggerFactory.getLogger(WCPSEvalVisitor.class);

    /**
     * expecting a link on the federated rasdaman
     */

    protected Map<String, List<Coverage>> variables = new HashMap<>();

    protected Query forWhereClauseQuery;

    private WCSAdapterAPI wcsAdapter;

    public WCPSEvalVisitor(WCSAdapterAPI wcsAdapter) {
        this.wcsAdapter = wcsAdapter;
    }

    public WCSAdapterAPI getWcsAdapter() {
        return wcsAdapter;
    }

    public void setWcsAdapter(WCSAdapterAPI wcsAdapter) {
        this.wcsAdapter = wcsAdapter;
    }

    // @Override
    // public Query visitForClauseLabel(ForClauseLabelContext ctx) {
    //
    // List<Coverage> coverages = ctx.identifier().stream().map(identifier -> {
    // List<Coverage> femmeCoverages = null;
    // try {
    // femmeCoverages =
    // this.wcsAdapter.getCoveragesByCoverageId(identifier.getText());
    // } catch (FemmeDatastoreException e) {
    // e.printStackTrace();
    // }
    // Coverage coverage = null;
    // if (femmeCoverages.size() == 0) {
    // coverage = new Coverage();
    // coverage.setCoverageId(identifier.getText());
    // }
    // else {
    // coverage = femmeCoverages.get(0);
    // }
    // return coverage;
    // }).collect(Collectors.toList());
    //
    // variables.put(ctx.coverageVariableName().getText(), coverages);
    //
    // Query query = super.visitForClauseLabel(ctx);
    //
    // query.setSplittedQuery(XWCPSEvalUtils.constructForQueries(ctx.coverageVariableName().getText(),
    // coverages));
    //
    // return query;
    // }

    @Override
    public Query visitDescribeCoverageExpressionLabel(DescribeCoverageExpressionLabelContext ctx) {
        Query query = super.visitDescribeCoverageExpressionLabel(ctx);

        String variable = ctx.coverageVariableName().getText();
        Map<Coverage, XwcpsReturnValue> describeCoverages = variables.get(variable).stream().map(coverage -> {
            try {
                // TODO: Return all coverages, not just the first one
                List<Coverage> coverages = wcsAdapter.getCoveragesByCoverageId(coverage.getCoverageId());
                String describeCoverage = "";
                XwcpsReturnValue result = new XwcpsReturnValue();

                if (coverages.size() > 0) {
                    describeCoverage = coverages.get(0).getMetadata();

                    result.setXwcpsValue("<coverage id='" + coverage.getCoverageId() + "'>"
                            + describeCoverage.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
                            + "</coverage>");
                }

                Entry<Coverage, XwcpsReturnValue> entry = new SimpleImmutableEntry<>(coverage, result);

                return entry;
            } catch (FemmeDatastoreException | FemmeClientException e) {
                logger.error(e.getMessage(), e);

                throw new ParseCancellationException(e);
            }
        }).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        query.getCoverageValueMap().clear();
        query.getCoverageValueMap().putAll(describeCoverages);
        return query.evaluated();
    }

    @Override
    public Query visitWcpsQuery(WcpsQueryContext ctx) {
        this.forWhereClauseQuery = visit(ctx.forClauseList());

        String xpathString = null;
        if (ctx.whereClause() != null) {
            Query whereClauseQuery = visit(ctx.whereClause());
            if (!whereClauseQuery.getXpath().equals("")) {
                xpathString = whereClauseQuery.getXpath();
            }
        }
        this.fetchMetadata(forWhereClauseQuery, xpathString);

        List<Coverage> coverages = new ArrayList<>(forWhereClauseQuery.getCoverageValueMap().keySet());
        forWhereClauseQuery.setSplittedQuery(XWCPSEvalUtils.constructForQueries(forWhereClauseQuery.getVariableName(), coverages));
        this.variables.put(forWhereClauseQuery.getVariableName(), coverages);

        if (ctx.orderByClause() != null) {
            Query orderByClauseQuery = visit(ctx.orderByClause());

            this.forWhereClauseQuery.getOrderedCoverages().clear();
            this.forWhereClauseQuery.getOrderedCoverages().addAll(orderByClauseQuery.getOrderedCoverages());
        }

        Query query = this.forWhereClauseQuery;

        Query returnClauseQuery = visit(ctx.returnClause());

        query.applyReturnToWhere(returnClauseQuery, false);

        // try {
        if (!query.isEvaluated()) {
            XwcpsQueryResult xwcpsQueryResult = new XwcpsQueryResult();
            // TODO: add processCoverages to wcs adapter.
            // xwcpsQueryResult.setAggregatedValue(
            // wcsRequestBuilder.processCoverages().query(query.getQuery()).build().get().getResponse());
            // if (xwcpsQueryResult.getMixedValues() != null) {
            // query.getMixedValues().addAll(xwcpsQueryResult.getMixedValues());
            // }

            return query.setValue(xwcpsQueryResult.getAggregatedValue());
        } else {
            return query;
        }
        // } catch (WCSRequestException e) {
        // logger.error(e.getMessage());
        // return query.setError(e.getError());
        // }
    }

    @Override
    public Query visitEncodedCoverageExpressionLabel(EncodedCoverageExpressionLabelContext ctx) {
        final Query encodedCoverageExpressionLabel = super.visitEncodedCoverageExpressionLabel(ctx);

        if (!forWhereClauseQuery.getSplittedQuery().isEmpty()) {
            Stream<Query> stream = forWhereClauseQuery.getSplittedQuery().stream();

            stream = !forWhereClauseQuery.getCoverageValueMap().isEmpty()
                    ? stream.filter(splitted -> !(Sets.intersection(splitted.getCoverageValueMap().keySet(),
                    forWhereClauseQuery.getCoverageValueMap().keySet()).isEmpty()))
                    : stream;

            List<Map<Coverage, XwcpsReturnValue>> resultsPerCoverageList = stream.map(forWhereClauseQuery -> {
                String rewrittenQuery = forWhereClauseQuery.getQuery() + " return "
                        + encodedCoverageExpressionLabel.getQuery();

                Map<Coverage, XwcpsReturnValue> resultByCoverage = null;
                try {
                    resultByCoverage = new HashMap<Coverage, XwcpsReturnValue>();

                    for (Entry<Coverage, XwcpsReturnValue> coverageEntry : forWhereClauseQuery.getCoverageValueMap()
                            .entrySet()) {
                        XwcpsReturnValue encodedResult = new XwcpsReturnValue();

                        // if (coverageEntry.getKey().getServers().size() == 0)
                        // continue;

                        WCSRequestBuilder wcsRequestBuilder = new WCSRequestBuilder()
                                .endpoint(coverageEntry.getKey().getServers().get(0).getEndpoint());
                        WCSResponse wcsResponce = null;
                        try {
                            wcsResponce = wcsRequestBuilder.processCoverages().query(rewrittenQuery).build().get();
                        } catch (WCSRequestException e) {
                            e.printStackTrace();
                            WCPSEvalVisitor.logger.error(e.getMessage(), e);
                        }

                        encodedResult.setWcpsValue(wcsResponce.getResponse());
                        encodedResult.setWcpsMediaType(wcsResponce.getContentType());
                        encodedResult.setSubQuery(rewrittenQuery);

                        resultByCoverage.put(coverageEntry.getKey(), encodedResult);
                    }

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                return resultByCoverage;
            }).collect(Collectors.toList());

            for (Map<Coverage, XwcpsReturnValue> resultPerCoverage : resultsPerCoverageList) {
                for (Entry<Coverage, XwcpsReturnValue> coverageEntry : resultPerCoverage.entrySet()) {
                    encodedCoverageExpressionLabel.getCoverageValueMap().put(coverageEntry.getKey(),
                            coverageEntry.getValue());
                }
            }

            // encodedCoverageExpressionLabel.setMixedValues(
            // stream.map(forWhereClauseQuery -> {
            // XwcpsReturnValue mixedValue = null;
            //
            // String rewrittenQuery = forWhereClauseQuery.getQuery() + " return
            // "
            // + encodedCoverageExpressionLabel.getQuery();
            //
            // try {
            // XwcpsQueryResult xwcpsQueryResult = new XwcpsQueryResult();
            //
            // WCSResponse wcsResponce = wcsRequestBuilder.processCoverages()
            // .query(rewrittenQuery).build().get();
            //
            // xwcpsQueryResult.setAggregatedValue(wcsResponce.getResponse());
            //
            // mixedValue = xwcpsQueryResult.getMixedValues().iterator().next();
            //
            // } catch (Exception e) {
            // logger.error(e.getMessage(), e);
            // mixedValue = new XwcpsReturnValue();
            // mixedValue.setXwcpsValue(e.getMessage());
            // }
            //
            // mixedValue.setSubQuery(rewrittenQuery);
            //
            // return mixedValue;
            // }).filter(mixedValue -> mixedValue !=
            // null).collect(Collectors.toSet())
            //
            // );

            encodedCoverageExpressionLabel.evaluated();
        }

        return encodedCoverageExpressionLabel;
    }

    @Override
    public abstract Query visitXwcps(XwcpsContext ctx);

    private void fetchMetadata(Query forWhereClauseQuery, String xpath) {
        List<Coverage> femmeCoverages = new ArrayList<>();

        forWhereClauseQuery.getCoverageValueMap().clear();
        for (ForClauseInfo forClauseInfo : forWhereClauseQuery.getForClauseInfo()) {
            switch (forClauseInfo.getForType()) {
                case ALL_COVERAGES:
                    try {
                        femmeCoverages.addAll(this.getWcsAdapter().getCoverages(null, null, xpath));
                        /*QueryOptionsFields options = new QueryOptionsFields();
                        Set<String> set = new HashSet<>();
                        set.add("metadata");
                        options.setInclude(set);
                        femmeCoverages.addAll(this.getWcsAdapter().findCoverages(null, options, xpath));*/

                    } catch (FemmeDatastoreException | FemmeClientException e) {
                    }

                    for (Coverage coverage : femmeCoverages) {
                        forWhereClauseQuery.getCoverageValueMap().put(coverage, new XwcpsReturnValue());
                    }
                    break;
                case ALL_COVERAGES_IN_SERVER:
                    try {
                        femmeCoverages.addAll(this.getWcsAdapter().getCoveragesInServer(
                                new ArrayList<>(Arrays.asList(forClauseInfo.getEndpoint())), null, null,
                                xpath));
                    } catch (FemmeDatastoreException | FemmeClientException e) {
                        e.printStackTrace();
                    }

                    for (Coverage coverage : femmeCoverages) {
                        forWhereClauseQuery.getCoverageValueMap().put(coverage, new XwcpsReturnValue());
                    }
                    break;
                case SPECIFIC_ID:
                    try {
                        femmeCoverages = this.getWcsAdapter().getCoveragesByCoverageId(forClauseInfo.getCoverageId());
                    } catch (FemmeDatastoreException | FemmeClientException e) {
                        e.printStackTrace();
                    }

                    if (femmeCoverages.size() > 0) {
                        forWhereClauseQuery.getCoverageValueMap().put(femmeCoverages.get(0), new XwcpsReturnValue());
                    }
                    break;
                case SPECIFIC_ID_IN_SERVER:
                    Coverage coverage = null;
                    try {
                        coverage = this.getWcsAdapter().getCoverageByCoverageIdInServer(forClauseInfo.getEndpoint(),
                                forClauseInfo.getCoverageId());
                    } catch (FemmeDatastoreException | FemmeClientException e) {
                        e.printStackTrace();
                    }
                    if (coverage != null) {
                        forWhereClauseQuery.getCoverageValueMap().put(coverage, new XwcpsReturnValue());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
