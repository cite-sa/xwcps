package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.Stack;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.wcps.grammar.XWCPSBaseVisitor;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.AndExprContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.EqualityExprContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.MainContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.OrExprContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.PredicateContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.RelativeLocationPathContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.StepContext;
import gr.cite.earthserver.wcps.parser.utils.XWCPSReservedWords;
import gr.cite.exmms.manager.core.DataElement;
import gr.cite.exmms.manager.core.DataElementMetadatum;
import gr.cite.exmms.manager.core.Metadatum;
import gr.cite.exmms.manager.criteria.CriteriaQuery;
import gr.cite.exmms.manager.criteria.Where;
import gr.cite.exmms.manager.criteria.WhereBuilder;

public class XpathForClauseEvalVisitor extends XWCPSBaseVisitor<XpathForClause> {
	private static final Logger logger = LoggerFactory.getLogger(XpathForClauseEvalVisitor.class);

	XpathForClauseEvalVisitorHelper helper = new XpathForClauseEvalVisitorHelper();

	private CriteriaQuery<DataElement> query;

	private boolean isSimpleXpath = false;

	public XpathForClauseEvalVisitor(CriteriaQuery<DataElement> query) {
		super();
		this.query = query;
	}

	@Override
	public XpathForClause visitMain(MainContext ctx) {

		Where<DataElement> whereRoot = query.whereBuilder();

		helper.pushWhereStack(whereRoot);

		try {
			return super.visitMain(ctx);
		} finally {
			helper.popWhereStack();
		}
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

		System.out.println(xpathStartingTerminal.getText() + simpleXpathContext.getText());

		return null;
	}

	@Override
	public XpathForClause visitOrExpr(OrExprContext ctx) {
		if (isSimpleXpath) {
			return super.visitOrExpr(ctx);
		}

		if (ctx.andExpr().size() > 1) {

			Where<DataElement> orWhere = query.expressionFactory();
			helper.pushWhereStack(orWhere);

			int i = 0;
			for (AndExprContext andExprContext : ctx.andExpr()) {

				visit(andExprContext);

				if (i + 1 < ctx.andExpr().size()) {
					helper.popWhereStack();
					helper.pushWhereStack(helper.popWhereBuilderStack().or());

				}

				++i;
			}

			helper.popWhereStack();
			helper.pushWhereBuilderStack(helper.peekWhereStack().expression(helper.popWhereBuilderStack()));

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

			Where<DataElement> andWhere = query.expressionFactory();
			helper.pushWhereStack(andWhere);

			int i = 0;
			for (EqualityExprContext equalityExprContext : ctx.equalityExpr()) {

				visit(equalityExprContext);

				if (i + 1 < ctx.equalityExpr().size()) {
					helper.popWhereStack();
					helper.pushWhereStack(helper.popWhereBuilderStack().and());
				}

				++i;
			}

			helper.popWhereStack();
			helper.pushWhereBuilderStack(helper.peekWhereStack().expression(helper.popWhereBuilderStack()));

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

				Where<DataElement> serverWhere = query.expressionFactory();
				helper.pushWhereStack(serverWhere);

				for (PredicateContext predicate : ctx.predicate()) {
					visit(predicate);
				}

				helper.popWhereStack();
				WhereBuilder<DataElement> serverWhereBuilder = helper.peekWhereStack()
						.isChildOf(helper.popWhereBuilderStack());
				helper.popWhereStack();

				helper.pushWhereStack(serverWhereBuilder.and());

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
		metadatum.setKey(key.replaceAll("@", ""));
		metadatum.setValue(value);

		helper.pushWhereBuilderStack(helper.peekWhereStack().expression(metadatum));

		// FIXME
		return null;
	}

}

class XpathForClauseEvalVisitorHelper {
	private Stack<WhereBuilder<DataElement>> whereBuilderStack = new Stack<>();
	private Stack<Where<DataElement>> whereStack = new Stack<>();

	public void pushWhereBuilderStack(WhereBuilder<DataElement> builder) {
		whereBuilderStack.push(builder);
	}

	public boolean isEmptyWhereStack() {
		return whereStack.isEmpty();
	}

	public boolean isEmptyWhereBuilderStack() {
		return whereBuilderStack.isEmpty();
	}

	public WhereBuilder<DataElement> peekWhereBuilderStack() {
		return whereBuilderStack.peek();
	}

	public WhereBuilder<DataElement> popWhereBuilderStack() {
		WhereBuilder<DataElement> pop = whereBuilderStack.pop();
		return pop;
	}

	public void pushWhereStack(Where<DataElement> builder) {
		whereStack.push(builder);
	}

	public Where<DataElement> popWhereStack() {
		Where<DataElement> pop = whereStack.pop();
		return pop;
	}

	public Where<DataElement> peekWhereStack() {
		return whereStack.peek();
	}

}
