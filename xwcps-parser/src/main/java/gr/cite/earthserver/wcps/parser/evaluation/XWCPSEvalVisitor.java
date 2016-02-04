package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathFactoryConfigurationException;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.wcps.grammar.XWCPSParser.AttributeContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.CloseXmlElementContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.OpenXmlElementContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.ProcessingExpressionContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XmlReturnClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XpathForClauseContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XqueryContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XwcpsContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XwcpsReturnClauseContext;
import gr.cite.earthserver.wcs.client.WCSRequestBuilder;
import gr.cite.earthserver.wcs.client.WCSRequestException;
import gr.cite.scarabaeus.utils.xml.XMLConverter;
import gr.cite.scarabaeus.utils.xml.XPathEvaluator;

public class XWCPSEvalVisitor extends WCPSEvalVisitor {
	private static final Logger logger = LoggerFactory.getLogger(XWCPSEvalVisitor.class);

	private Stack<String> xmlReturnElements = new Stack<>();

	public XWCPSEvalVisitor(String wcsEndpoint) {
		super(wcsEndpoint);
	}

	public XWCPSEvalVisitor(WCSRequestBuilder wcsRequestBuilder) {
		super(wcsRequestBuilder);
	}

	@Override
	public Query visitXwcps(XwcpsContext ctx) {
		if (ctx.wcpsQuery() != null) {
			return visit(ctx.wcpsQuery());
		}
		return visit(ctx.xquery());
	}

	@Override
	public Query visitXquery(XqueryContext ctx) {
		Query query = super.visitXquery(ctx);

		return query;
	}

	@Override
	public Query visitXpathForClause(XpathForClauseContext ctx) {
		Query q = visit(ctx.coverageVariableName());
		
		XpathForClauseEvalVisitor xpathForClauseEvalVisitor = new XpathForClauseEvalVisitor();
		xpathForClauseEvalVisitor.visit(ctx);
		
		return q;
	}

	@Override
	public Query visitXwcpsReturnClause(XwcpsReturnClauseContext ctx) {

		Query wcpsQuery = visit(ctx.scalarExpression());

		if (ctx.xquery() == null) {

			if (ctx.scalarExpression().getComponentExpression() != null) {
				return wcpsQuery;
			} else {
				// wcps query with scalar return expression 
				try {

					String rewrittedQuery = this.forWhereClauseQuery.getQuery() + " return " + wcpsQuery.getQuery();

					if (ctx.getParent() instanceof ProcessingExpressionContext) {
						wcpsQuery.simpleWCPS();
					}

					return wcpsQuery.evaluated()
							.setValue(wcsRequestBuilder.processCoverages().query(rewrittedQuery).build().get());
				} catch (WCSRequestException e) {
					logger.error(e.getMessage(), e);

					throw new ParseCancellationException(e);
				}
			}

		}

		Query xpathQuery = visit(ctx.xquery());

		if (ctx.functionName() != null) {
			xpathQuery.prependQuery(ctx.functionName().getText() + "(").appendQuery(")");
		}

		try {
			return evaluateXpath(wcpsQuery, xpathQuery);
		} catch (XPathFactoryConfigurationException e) {
			logger.error(e.getMessage(), e);

			throw new ParseCancellationException(e);
		}
	}

	@Override
	public Query visitOpenXmlElement(OpenXmlElementContext ctx) {
		String xmlElement = ctx.qName().getText();
		xmlReturnElements.push(xmlElement);

		Query query = visit(ctx.LOWER_THAN()).setValue(ctx.LOWER_THAN().getText());

		query.aggregate(visit(ctx.qName()).setValue(ctx.qName().getText()));

		for (AttributeContext attributeContext : ctx.attribute()) {
			query.setValue(query.getValue() + " ");
			query.aggregate(visit(attributeContext));
		}

		query.aggregate(visit(ctx.GREATER_THAN()).setValue(ctx.GREATER_THAN().getText()));

		return query;
	}

	@Override
	public Query visitCloseXmlElement(CloseXmlElementContext ctx) {
		return super.visitCloseXmlElement(ctx).setValue(ctx.getText());
	}

	@Override
	public Query visitXmlReturnClause(XmlReturnClauseContext ctx) {
		Query q = super.visitXmlReturnClause(ctx);
		return q;
	}

	@Override
	public Query visitAttribute(AttributeContext ctx) {
		Query q = visit(ctx.qName()).setValue(ctx.qName().getText());
		q.aggregate(visit(ctx.EQUAL()).setValue(ctx.EQUAL().getText()));

		if (ctx.quated() != null) {
			q.aggregate(visit(ctx.quated()).setValue(ctx.quated().getText()));
		} else {
			q.setValue(q.getValue() + "\"");
			q.aggregate(visit(ctx.xwcpsReturnClause()));
			q.setValue(q.getValue() + "\"");
		}

		return q;
	}

	@Override
	public Query visitProcessingExpression(ProcessingExpressionContext ctx) {
		if (ctx.xwcpsReturnClause() != null) {
			Query xwcpsQuery = visit(ctx.xwcpsReturnClause());

			return xwcpsQuery.isSimpleWCPS() ? xwcpsQuery : XWCPSEvalUtils.wrapDefaultXmlReturnElement(xwcpsQuery);
		}

		return super.visitProcessingExpression(ctx);
	}

	private static Query evaluateXpath(Query wcpsQuery, Query xpathQuery) throws XPathFactoryConfigurationException {
		XPathEvaluator xPathEvaluator = new XPathEvaluator(XMLConverter.stringToNode(wcpsQuery.getValue(), true));

		List<String> xpathResults = xPathEvaluator.evaluate(xpathQuery.getQuery());

		String value = xpathResults.stream().collect(Collectors.joining(" "));

		return wcpsQuery.aggregate(xpathQuery.setValue(value), true);
	}
}
