package gr.cite.earthserver.wcps.parser.evaluation.visitors;

import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.grammar.XWCPSBaseVisitor;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.AndExprContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.EqualityExprContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.MainContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.OrExprContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.PredicateContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.RelativeLocationPathContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.StepContext;
import gr.cite.earthserver.wcps.parser.evaluation.XpathForClause;
import gr.cite.earthserver.wcps.parser.utils.PrintVisitor;
import gr.cite.earthserver.wcps.parser.utils.XWCPSEvalUtils;
import gr.cite.earthserver.wcps.parser.utils.XWCPSReservedWords;
import gr.cite.exmms.core.DataElementMetadatum;
import gr.cite.exmms.core.Metadatum;
import gr.cite.exmms.criteria.CriteriaQuery;
import gr.cite.exmms.criteria.Where;
import gr.cite.exmms.criteria.WhereBuilder;

public class XpathForClauseEvalVisitor extends XWCPSBaseVisitor<XpathForClause> {
	private static final Logger logger = LoggerFactory.getLogger(XpathForClauseEvalVisitor.class);

	XpathForClauseEvalVisitorStack stack = new XpathForClauseEvalVisitorStack();

	private CriteriaQuery<Coverage> query;

	private boolean isSimpleXpath = false;

	public XpathForClauseEvalVisitor(CriteriaQuery<Coverage> query) {
		super();
		this.query = query;
	}

	XpathForClause executeQuery() {
		if (!stack.isEmptyWhereBuilderStack()) {
			this.query = stack.popWhereBuilderStack().build();
		}

		List<Coverage> coverages = this.query.find();

		return new XpathForClause().setCoverages(coverages);
	}

	@Override
	public XpathForClause visitMain(MainContext ctx) {

		Where<Coverage> whereRoot = query.whereBuilder();
		stack.pushWhereStack(whereRoot);

		XpathForClause visitMain = super.visitMain(ctx);

		stack.popWhereStack();

		return visitMain.aggregate(executeQuery());

	}

	@Override
	public XpathForClause visitRelativeLocationPath(RelativeLocationPathContext ctx) {

		StepContext simpleXpathContext = null;
		TerminalNode xpathStartingTerminal = null;

		for (ParseTree parseTree : ctx.children) {
			if (isSimpleXpath && (parseTree instanceof TerminalNode)) {
				xpathStartingTerminal = (TerminalNode) parseTree;
				continue;
			} else if (isSimpleXpath && (parseTree instanceof StepContext)) {
				simpleXpathContext = (StepContext) parseTree;
				break;
			}
			visit(parseTree);
		}

		XpathForClause xpathForClause = new XpathForClause();

		if (simpleXpathContext != null) {
			PrintVisitor printVisitor = new PrintVisitor();
			String xpath = printVisitor.visit(simpleXpathContext).toString();

			xpath = xpathStartingTerminal.getText() + xpath;
			logger.debug("xpath on coverage metadata: " + xpath);

			xpathForClause.setXpathQuery(xpath);
		}
		return xpathForClause;
	}

	@Override
	public XpathForClause visitOrExpr(OrExprContext ctx) {
		if (isSimpleXpath) {
			return super.visitOrExpr(ctx);
		}

		if (ctx.andExpr().size() > 1) {

			Where<Coverage> orWhere = query.expressionFactory();
			stack.pushWhereStack(orWhere);

			int i = 0;
			for (AndExprContext andExprContext : ctx.andExpr()) {

				visit(andExprContext);

				if (i + 1 < ctx.andExpr().size()) {
					stack.popWhereStack();
					stack.pushWhereStack(stack.popWhereBuilderStack().or());

				}

				++i;
			}

			stack.popWhereStack();
			stack.pushWhereBuilderStack(stack.peekWhereStack().expression(stack.popWhereBuilderStack()));

			return null;
		} else {
			return super.visitOrExpr(ctx);
		}
	}

	@Override
	public XpathForClause visitAndExpr(AndExprContext ctx) {
		if (isSimpleXpath) {
			return super.visitAndExpr(ctx);
		}

		if (ctx.equalityExpr().size() > 1) {

			Where<Coverage> andWhere = query.expressionFactory();
			stack.pushWhereStack(andWhere);

			int i = 0;
			for (EqualityExprContext equalityExprContext : ctx.equalityExpr()) {

				visit(equalityExprContext);

				if (i + 1 < ctx.equalityExpr().size()) {
					stack.popWhereStack();
					stack.pushWhereStack(stack.popWhereBuilderStack().and());
				}

				++i;
			}

			stack.popWhereStack();
			stack.pushWhereBuilderStack(stack.peekWhereStack().expression(stack.popWhereBuilderStack()));

			return null;
		} else {
			return super.visitAndExpr(ctx);
		}
	}

	@Override
	public XpathForClause visitStep(StepContext ctx) {

		if (isSimpleXpath) {
			return super.visitStep(ctx);
		}

		boolean foundCoverage = false;

		String axisSpecifier = ctx.axisSpecifier().getText();
		String nodeName = ctx.nodeTest().getText();

		if (axisSpecifier.isEmpty()) {
			// is node

			switch (nodeName) {
			case XWCPSReservedWords.COVERAGE:
				foundCoverage = true;

				for (PredicateContext predicate : ctx.predicate()) {
					visit(predicate);
				}

				break;
			case XWCPSReservedWords.SERVER:

				Where<Coverage> serverWhere = query.expressionFactory();
				stack.pushWhereStack(serverWhere);

				for (PredicateContext predicate : ctx.predicate()) {
					visit(predicate);
				}

				stack.popWhereStack();
				WhereBuilder<Coverage> serverWhereBuilder = stack.peekWhereStack()
						.isChildOf(stack.popWhereBuilderStack());
				stack.popWhereStack();

				stack.pushWhereStack(serverWhereBuilder.and());

				break;
			default:
				throw new ParseCancellationException(
						"Expecting an xpath with '" + XWCPSReservedWords.COVERAGE + "' element");
			}

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
		metadatum.setName(key.replaceAll("@", ""));
		metadatum.setValue(XWCPSEvalUtils.removeQuates(value));

		stack.pushWhereBuilderStack(stack.peekWhereStack().expression(metadatum));

		// FIXME
		return null;
	}

	@Override
	protected XpathForClause aggregateResult(XpathForClause aggregate, XpathForClause nextResult) {
		if (aggregate == null) {
			return nextResult;
		} else if (nextResult == null) {
			return aggregate;
		}
		return aggregate.aggregate(nextResult);
	}

}

class XpathForClauseEvalVisitorStack {
	private Stack<WhereBuilder<Coverage>> whereBuilderStack = new Stack<>();
	private Stack<Where<Coverage>> whereStack = new Stack<>();

	public void pushWhereBuilderStack(WhereBuilder<Coverage> builder) {
		whereBuilderStack.push(builder);
	}

	public boolean isEmptyWhereStack() {
		return whereStack.isEmpty();
	}

	public boolean isEmptyWhereBuilderStack() {
		return whereBuilderStack.isEmpty();
	}

	public WhereBuilder<Coverage> peekWhereBuilderStack() {
		return whereBuilderStack.peek();
	}

	public WhereBuilder<Coverage> popWhereBuilderStack() {
		WhereBuilder<Coverage> pop = whereBuilderStack.pop();
		return pop;
	}

	public void pushWhereStack(Where<Coverage> builder) {
		whereStack.push(builder);
	}

	public Where<Coverage> popWhereStack() {
		Where<Coverage> pop = whereStack.pop();
		return pop;
	}

	public Where<Coverage> peekWhereStack() {
		return whereStack.peek();
	}

}
