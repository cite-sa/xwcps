package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	WCSRequestBuilder wcsRequestBuilder;

	Map<String, List<String>> variables = new HashMap<>();

	Query forWhereClauseQuery;

	public WCPSEvalVisitor(String wcsEndpoint) {
		this.wcsRequestBuilder = WCSRequest.newBuilder().endpoint(wcsEndpoint);
	}

	public WCPSEvalVisitor(WCSRequestBuilder wcsRequestBuilder) {
		this.wcsRequestBuilder = wcsRequestBuilder;
	}

	@Override
	public Query visitForClauseLabel(ForClauseLabelContext ctx) {

		variables.put(ctx.coverageVariableName().getText(),
				ctx.identifier().stream().map(IdentifierContext::getText).collect(Collectors.toList()));

		return super.visitForClauseLabel(ctx);
	}

	@Override
	public Query visitDescribeCoverageExpressionLabel(DescribeCoverageExpressionLabelContext ctx) {
		Query query = super.visitDescribeCoverageExpressionLabel(ctx);

		String variable = ctx.coverageVariableName().getText();
		List<String> describeCoverages = variables.get(variable).stream().map(coverage -> {
			try {
				String describeCoverage = wcsRequestBuilder.describeCoverage().coverageId(coverage).build().get();

				return "<coverage id='" + coverage + "'>"
						+ describeCoverage.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "") + "</coverage>";
			} catch (WCSRequestException e) {
				logger.error(e.getMessage(), e);

				throw new ParseCancellationException(e);
			}
		}).collect(Collectors.toList());

		return query.evaluated()
				.setValue("<coverages>" + describeCoverages.stream().collect(Collectors.joining()) + "</coverages>");
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
