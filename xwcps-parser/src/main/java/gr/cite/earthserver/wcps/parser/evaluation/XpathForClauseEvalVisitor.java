package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.wcps.grammar.XWCPSBaseVisitor;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.AbsoluteLocationPathNorootContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.AndExprContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.EqualityExprContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.PredicateContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.RelationalExprContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.RelativeLocationPathContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.StepContext;
import gr.cite.earthserver.wcps.parser.utils.XWCPSReservedWords;
import gr.cite.exmms.manager.core.Collection;
import gr.cite.exmms.manager.core.DataElement;
import gr.cite.exmms.manager.core.DataElementMetadatum;
import gr.cite.exmms.manager.core.Metadatum;
import gr.cite.exmms.manager.criteria.CriteriaQuery;
import gr.cite.exmms.manager.criteria.Where;
import gr.cite.exmms.manager.criteria.WhereBuilder;

public class XpathForClauseEvalVisitor extends XWCPSBaseVisitor<XpathForClause> {
	private static final Logger logger = LoggerFactory.getLogger(XpathForClauseEvalVisitor.class);

	private CriteriaQuery<DataElement> query;
	private Stack<WhereBuilder<DataElement>> whereBuilderStack = new Stack<>();
	private Stack<Where<DataElement>> whereStack = new Stack<>();

	private boolean isSimpleXpath = false;

	public XpathForClauseEvalVisitor(CriteriaQuery<DataElement> query) {
		super();
		this.query = query;
	}

	@Override
	public XpathForClause visitAbsoluteLocationPathNoroot(AbsoluteLocationPathNorootContext ctx) {

		System.out.println("--> " + ctx.getText());
		// TODO Auto-generated method stub
		return super.visitAbsoluteLocationPathNoroot(ctx);
	}

	@Override
	public XpathForClause visitAndExpr(AndExprContext ctx) {
		if (ctx.equalityExpr().size() > 1) {
			// TODO
			System.out.println("XpathForClauseEvalVisitor.visitAndExpr()");
			System.out.println("\t" + ctx.equalityExpr(0).getText());
			System.out.println("\t" + ctx.equalityExpr(1).getText());
		}

		// TODO Auto-generated method stub
		return super.visitAndExpr(ctx);
	}

	@Override
	public XpathForClause visitRelativeLocationPath(RelativeLocationPathContext ctx) {

		List<StepContext> steps = ctx.step();
		for (StepContext stepContext : steps) {
			System.out.println("stepContext ::  " + stepContext.getText());
		}

		// TODO Auto-generated method stub
		return super.visitRelativeLocationPath(ctx);
	}

	@Override
	public XpathForClause visitStep(StepContext ctx) {

		if (isSimpleXpath) {
			// TODO Auto-generated method stub
			return super.visitStep(ctx);
		}

		boolean foundCoverage = false;

		String axisSpecifier = ctx.axisSpecifier().getText();
		String nodeName = ctx.nodeTest().getText();

		if (axisSpecifier.isEmpty()) {
			// is node
			logger.debug(" / " + nodeName);

			Where<DataElement> whereRoot ;
			WhereBuilder<DataElement> whereBuilderRoot ;

			
			switch (nodeName) {
			case XWCPSReservedWords.COVERAGE:
				foundCoverage = true;
				
				if (!ctx.predicate().isEmpty()) {
					whereBuilderRoot = whereBuilderStack.pop();
					whereBuilderRoot.and();
				}
				
				for (PredicateContext predicate : ctx.predicate()) {
					visit(predicate);
				}
				
				break;
			case XWCPSReservedWords.SERVER:
				//push where root
				whereRoot = query.whereBuilder();
				whereStack.push(whereRoot);
				
				Where<DataElement> serverWhere = query.expressionFactory();
				whereStack.push(serverWhere);
				
				for (PredicateContext predicate : ctx.predicate()) {
					visit(predicate);
				}

				WhereBuilder<DataElement> serverWhereBuilder = whereRoot.isChildOf(whereBuilderStack.pop());
				whereBuilderStack.push(serverWhereBuilder);
				
				whereStack.pop();

				break;
			default:
				throw new ParseCancellationException(
						"Expecting an xpath with '" + XWCPSReservedWords.COVERAGE + "' element");
			}

		} else if (axisSpecifier.startsWith("@")) {
			// is attribute
			logger.info(" @ " + nodeName);
			// TODO
		} else {
			// is functions
			// TODO
		}

		if (foundCoverage) {
			isSimpleXpath = true;
		}

		// FIXME
		return null;

	}

	@Override
	public XpathForClause visitEqualityExpr(EqualityExprContext ctx) {
		if (isSimpleXpath) {
			return super.visitEqualityExpr(ctx);
		}

		if (ctx.relationalExpr().size() == 1) {
			return super.visitEqualityExpr(ctx);
		}

		String key = ctx.relationalExpr(0).getText();
		String value = ctx.relationalExpr(1).getText();

		Metadatum metadatum = new DataElementMetadatum();
		metadatum.setKey(key.replaceAll("@", ""));
		metadatum.setValue(value);

		if (whereBuilderStack.isEmpty()) {
			whereBuilderStack.push(whereStack.peek().expression(metadatum));
		} else {
			WhereBuilder<DataElement> where = whereBuilderStack.peek();
			where.and().expression(metadatum);
		}

		// FIXME
		return null;
	}

}
