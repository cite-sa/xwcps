package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.DescribeCoverageExpressionLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.ForClauseLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.IdentifierContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.WcpsQueryLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XwcpsContext;
import gr.cite.earthserver.wcs.client.WCSRequest;
import gr.cite.earthserver.wcs.client.WCSRequestBuilder;
import gr.cite.earthserver.wcs.client.WCSRequestException;

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
	 * @param wcsEndpoint url on the federated rasdaman
	 */
	public WCPSEvalVisitor(String wcsEndpoint) {
		this.wcsRequestBuilder = WCSRequest.newBuilder().endpoint(wcsEndpoint);
	}

	/**
	 * 
	 * @param wcsRequestBuilder wcs request builder with url on the federated rasdaman
	 */
	public WCPSEvalVisitor(WCSRequestBuilder wcsRequestBuilder) {
		this.wcsRequestBuilder = wcsRequestBuilder;
	}

	@Override
	public Query visitForClauseLabel(ForClauseLabelContext ctx) {

		variables.put(ctx.coverageVariableName().getText(), ctx.identifier().stream().map(identifier -> {
			Coverage c = new Coverage();
			c.setLocalId(identifier.getText());
			return c;
		}).collect(Collectors.toList()));

		return super.visitForClauseLabel(ctx);
	}

	@Override
	public Query visitDescribeCoverageExpressionLabel(DescribeCoverageExpressionLabelContext ctx) {
		Query query = super.visitDescribeCoverageExpressionLabel(ctx);

		String variable = ctx.coverageVariableName().getText();
		Map<Coverage, String> describeCoverages = variables.get(variable).stream().map(coverage -> {
			try {
				String describeCoverage = wcsRequestBuilder.describeCoverage().coverageId(coverage.getLocalId()).build()
						.get();

				Entry<Coverage, String> entry = new SimpleImmutableEntry<>(coverage, "<coverage id='" + coverage + "'>"
						+ describeCoverage.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "") + "</coverage>");

				return entry;
			} catch (WCSRequestException e) {
				logger.error(e.getMessage(), e);

				throw new ParseCancellationException(e);
			}
		}).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

		return query.evaluated().setCoverageValueMap(describeCoverages);
				//.setValue("<coverages>" + describeCoverages.stream().collect(Collectors.joining()) + "</coverages>");
	}

	@Override
	public Query visitWcpsQueryLabel(WcpsQueryLabelContext ctx) {
		this.forWhereClauseQuery = visit(ctx.forClauseList());
		if (ctx.whereClause() != null) {
			this.forWhereClauseQuery.aggregate(visit(ctx.whereClause()));
		}

		Query query = this.forWhereClauseQuery;

		query.aggregate(visit(ctx.returnClause()));

		try {
			if (!query.isEvaluated()) {
				return query.setValue(wcsRequestBuilder.processCoverages().query(query.getQuery()).build().get());
			} else {
				return query;
			}
		} catch (WCSRequestException e) {
			logger.error(e.getMessage());
			return query.setError(e.getError());
		}
	}

	@Override
	public abstract Query visitXwcps(XwcpsContext ctx);
}
