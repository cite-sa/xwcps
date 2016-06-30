package gr.cite.earthserver.wcps.parser.evaluation.visitors;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.DescribeCoverageExpressionLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.EncodedCoverageExpressionLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.ForClauseLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.WcpsQueryContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XwcpsContext;
import gr.cite.earthserver.wcps.parser.core.XwcpsQueryResult;
import gr.cite.earthserver.wcps.parser.core.XwcpsReturnValue;
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcps.parser.utils.XWCPSEvalUtils;
import gr.cite.earthserver.wcs.client.WCSRequest;
import gr.cite.earthserver.wcs.client.WCSRequestBuilder;
import gr.cite.earthserver.wcs.client.WCSRequestException;
import gr.cite.earthserver.wcs.client.WCSResponse;

public abstract class WCPSEvalVisitor extends XWCPSParseTreeVisitor {
	private static final Logger logger = LoggerFactory.getLogger(WCPSEvalVisitor.class);

	/**
	 * expecting a link on the federated rasdaman
	 */

	WCSRequestBuilder wcsRequestBuilder;

	Map<String, List<Coverage>> variables = new HashMap<>();

	Query forWhereClauseQuery;

	/**
	 * 
	 * @param wcsEndpoint
	 *            url on the federated rasdaman
	 */
	public WCPSEvalVisitor(String wcsEndpoint) {
		this.wcsRequestBuilder = WCSRequest.newBuilder().endpoint(wcsEndpoint);
	}

	/**
	 * 
	 * @param wcsRequestBuilder
	 *            wcs request builder with url on the federated rasdaman
	 */
	public WCPSEvalVisitor(WCSRequestBuilder wcsRequestBuilder) {
		this.wcsRequestBuilder = wcsRequestBuilder;
	}

	@Override
	public Query visitForClauseLabel(ForClauseLabelContext ctx) {

		List<Coverage> coverages = ctx.identifier().stream().map(identifier -> {
			Coverage c = new Coverage();
			c.setLocalId(identifier.getText());
			return c;
		}).collect(Collectors.toList());

		variables.put(ctx.coverageVariableName().getText(), coverages);

		Query query = super.visitForClauseLabel(ctx);

		query.setSplittedQuery(XWCPSEvalUtils.constructForQueries(ctx.coverageVariableName().getText(), coverages));

		return query;
	}

	@Override
	public Query visitDescribeCoverageExpressionLabel(DescribeCoverageExpressionLabelContext ctx) {
		Query query = super.visitDescribeCoverageExpressionLabel(ctx);

		String variable = ctx.coverageVariableName().getText();
		Map<Coverage, XwcpsReturnValue> describeCoverages = variables.get(variable).stream().map(coverage -> {
			try {
				String describeCoverage = wcsRequestBuilder.describeCoverage().coverageId(coverage.getLocalId()).build()
						.get().getResponse();

				XwcpsReturnValue result = new XwcpsReturnValue();
				result.setXwcpsValue("<coverage id='" + coverage + "'>"
						+ describeCoverage.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "") + "</coverage>");

				Entry<Coverage, XwcpsReturnValue> entry = new SimpleImmutableEntry<>(coverage, result);

				return entry;
			} catch (WCSRequestException e) {
				logger.error(e.getMessage(), e);

				throw new ParseCancellationException(e);
			}
		}).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

		return query.evaluated().setCoverageValueMap(describeCoverages);
		// .setValue("<coverages>" +
		// describeCoverages.stream().collect(Collectors.joining()) +
		// "</coverages>");
	}

	@Override
	public Query visitWcpsQuery(WcpsQueryContext ctx) {
		this.forWhereClauseQuery = visit(ctx.forClauseList());
		if (ctx.whereClause() != null) {
			this.forWhereClauseQuery.aggregate(visit(ctx.whereClause()));
		}

		Query query = this.forWhereClauseQuery;

		Query returnClauseQuery = visit(ctx.returnClause());

		query.aggregate(returnClauseQuery);

		try {
			if (!query.isEvaluated()) {
				XwcpsQueryResult xwcpsQueryResult = new XwcpsQueryResult();
				xwcpsQueryResult.setAggregatedValue(
						wcsRequestBuilder.processCoverages().query(query.getQuery()).build().get().getResponse());

				if (xwcpsQueryResult.getMixedValues() != null) {
					query.getMixedValues().addAll(xwcpsQueryResult.getMixedValues());
				}

				return query.setValue(xwcpsQueryResult.getAggregatedValue());
			} else {
				return query;
			}
		} catch (WCSRequestException e) {
			logger.error(e.getMessage());
			return query.setError(e.getError());
		}
	}

	@Override
	public Query visitEncodedCoverageExpressionLabel(EncodedCoverageExpressionLabelContext ctx) {
		final Query encodedCoverageExpressionLabel = super.visitEncodedCoverageExpressionLabel(ctx);

		if (!forWhereClauseQuery.getSplittedQuery().isEmpty()) {
			Stream<Query> stream = forWhereClauseQuery.getSplittedQuery().stream();

			stream = forWhereClauseQuery.getCoverageValueMap() != null
					? stream.filter(splitted -> !(Sets.intersection(splitted.getCoverageValueMap().keySet(),
							forWhereClauseQuery.getCoverageValueMap().keySet()).isEmpty()))
					: stream;

			List<Map<Coverage, XwcpsReturnValue>> resultsPerCoverageList = stream.map(forWhereClauseQuery -> {
				String rewrittenQuery = forWhereClauseQuery.getQuery() + " return "
						+ encodedCoverageExpressionLabel.getQuery();

				Map<Coverage, XwcpsReturnValue> resultByCoverage = null;
				try {
					resultByCoverage = new HashMap<Coverage, XwcpsReturnValue>();

					WCSResponse wcsResponce = wcsRequestBuilder.processCoverages().query(rewrittenQuery).build().get();

					for (Entry<Coverage, XwcpsReturnValue> coverageEntry : forWhereClauseQuery.getCoverageValueMap()
							.entrySet()) {
						XwcpsReturnValue encodedResult = new XwcpsReturnValue();

						encodedResult.setWcpsValue(
								new ByteArrayInputStream(wcsResponce.getResponse().getBytes(StandardCharsets.UTF_8)));
						encodedResult.setWcpsMediaType(wcsResponce.getContentType());
						encodedResult.setSubQuery(rewrittenQuery);

						resultByCoverage.put(coverageEntry.getKey(), encodedResult);
					}

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					// mixedValue = new XwcpsReturnValue();
					// mixedValue.setXwcpsValue(e.getMessage());
				}
				return resultByCoverage;
			}).collect(Collectors.toList());

			if (encodedCoverageExpressionLabel.getCoverageValueMap() == null) encodedCoverageExpressionLabel.setCoverageValueMap(new HashMap<Coverage, XwcpsReturnValue>());
			
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
}
