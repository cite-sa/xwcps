package gr.cite.earthserver.wcps.parser.evaluation.visitors;

import java.util.List;

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
import gr.cite.earthserver.wcps.parser.evaluation.XpathForClause;
import gr.cite.earthserver.wcps.parser.utils.PrintVisitor;
import gr.cite.earthserver.wcps.parser.utils.XWCPSEvalUtils;
import gr.cite.earthserver.wcps.parser.utils.XWCPSReservedWords;
import gr.cite.earthserver.wcs.adapter.api.WCSAdapterAPI;
import gr.cite.earthserver.wcs.adapter.request.WCSAdapterCoverages;
import gr.cite.earthserver.wcs.adapter.request.WCSAdapterRequest;
import gr.cite.earthserver.wcs.adapter.request.WCSAdapterRequestBuilder;
import gr.cite.earthserver.wcs.adapter.request.WCSAdapterServers;
import gr.cite.earthserver.wcs.core.Coverage;
import gr.cite.femme.client.FemmeDatastoreException;
import gr.cite.femme.model.Metadatum;
import gr.cite.femme.utils.Pair;

public class XpathForClauseEvalVisitor extends XWCPSBaseVisitor<XpathForClause> {
	private static final Logger logger = LoggerFactory.getLogger(XpathForClauseEvalVisitor.class);

	private boolean isSimpleXpath = false;

	private WCSAdapterServers myServers = WCSAdapterRequestBuilder.request().servers();

	private WCSAdapterCoverages myCoverages = WCSAdapterRequestBuilder.request().coverages();
	
	private WCSAdapterAPI wcsAdapter;

	private static enum State {
		SERVER, COVERAGE;
	}

	private State currentState = XpathForClauseEvalVisitor.State.SERVER;

	public XpathForClauseEvalVisitor(WCSAdapterAPI wcsAdapter) {
		super();
		this.wcsAdapter = wcsAdapter;
	}

	public XpathForClause executeQuery() {
//		if (!this.stack.isEmptyWhereBuilderStack()) {
//			this.query = this.stack.popWhereBuilderStack().build();
//		}

		//List<Coverage> coverages = this.query.find();
		
		WCSAdapterRequest request = new WCSAdapterRequest(this.myServers, this.myCoverages);
		
		List<Coverage> coverages = null;
		try {
			coverages = this.wcsAdapter.findCoverages(request.mapToQuery(), 5, null, null);
		} catch (FemmeDatastoreException e) {
			e.printStackTrace();
			XpathForClauseEvalVisitor.logger.debug("query has fucked everything: " + e.getMessage());
		}

		return new XpathForClause().setCoverages(coverages);
	}

	@Override
	public XpathForClause visitMain(MainContext ctx) {

//		Where<Coverage> whereRoot = this.query.whereBuilder();
//		this.stack.pushWhereStack(whereRoot);

		XpathForClause visitMain = super.visitMain(ctx);

//		this.stack.popWhereStack();

		XpathForClause executedQuery = this.executeQuery();

		return visitMain.aggregate(executedQuery);
	}

	@Override
	public XpathForClause visitRelativeLocationPath(RelativeLocationPathContext ctx) {

		StepContext simpleXpathContext = null;
		TerminalNode xpathStartingTerminal = null;

		for (ParseTree parseTree : ctx.children) {
			if (this.isSimpleXpath && (parseTree instanceof TerminalNode)) {
				xpathStartingTerminal = (TerminalNode) parseTree;
				continue;
			} else if (this.isSimpleXpath && (parseTree instanceof StepContext)) {
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
		if (this.isSimpleXpath) {
			return super.visitOrExpr(ctx);
		}

		switch (this.currentState) {
		case COVERAGE: {
			this.myCoverages.or();
			break;
		}
		case SERVER: {
			this.myServers.or();
			break;
		}
		default:
			break;
		}

		if (ctx.andExpr().size() > 1) {

			//Where<Coverage> orWhere = this.query.expressionFactory();
			//this.stack.pushWhereStack(orWhere);

			int i = 0;
			for (AndExprContext andExprContext : ctx.andExpr()) {

				visit(andExprContext);

				if (i + 1 < ctx.andExpr().size()) {
					//this.stack.popWhereStack();
					//this.stack.pushWhereStack(this.stack.popWhereBuilderStack().or());
				}

				++i;
			}

			//this.stack.popWhereStack();
			//this.stack.pushWhereBuilderStack(this.stack.peekWhereStack().expression(this.stack.popWhereBuilderStack()));

			return null;
		} else {
			return super.visitOrExpr(ctx);
		}
	}

	@Override
	public XpathForClause visitAndExpr(AndExprContext ctx) {
		if (this.isSimpleXpath) {
			return super.visitAndExpr(ctx);
		}

		switch (this.currentState) {
		case COVERAGE: {
			//this.myCoverages.and();
			break;
		}
		case SERVER: {
			//this.myServers.and();
			break;
		}
		default:
			break;
		}

		if (ctx.equalityExpr().size() > 1) {

//			Where<Coverage> andWhere = this.query.expressionFactory();
			//this.stack.pushWhereStack(andWhere);

			int i = 0;
			for (EqualityExprContext equalityExprContext : ctx.equalityExpr()) {

				visit(equalityExprContext);

				if (i + 1 < ctx.equalityExpr().size()) {
					//this.stack.popWhereStack();
					//this.stack.pushWhereStack(this.stack.popWhereBuilderStack().and());
				}

				++i;
			}

			//this.stack.popWhereStack();
			//this.stack.pushWhereBuilderStack(this.stack.peekWhereStack().expression(this.stack.popWhereBuilderStack()));

			return null;
		} else {
			return super.visitAndExpr(ctx);
		}
	}

	@Override
	public XpathForClause visitStep(StepContext ctx) {

		if (this.isSimpleXpath) {
			return super.visitStep(ctx);
		}

		boolean foundCoverage = false;

		String axisSpecifier = ctx.axisSpecifier().getText();
		String nodeName = ctx.nodeTest().getText();

		if (axisSpecifier.isEmpty()) {
			// is node

			switch (nodeName) {
			case XWCPSReservedWords.COVERAGE:
				this.currentState = XpathForClauseEvalVisitor.State.COVERAGE;
				foundCoverage = true;

				for (PredicateContext predicate : ctx.predicate()) {
					visit(predicate);
				}

				break;
			case XWCPSReservedWords.SERVER:
				this.currentState = XpathForClauseEvalVisitor.State.SERVER;

				//Where<Coverage> serverWhere = this.query.expressionFactory();
				//this.stack.pushWhereStack(serverWhere);

				for (PredicateContext predicate : ctx.predicate()) {
					visit(predicate);
				}

				//this.stack.popWhereStack();
				//WhereBuilder<Coverage> serverWhereBuilder = this.stack.peekWhereStack()
				//		.isChildOf(this.stack.popWhereBuilderStack());
				//this.stack.popWhereStack();

				//this.stack.pushWhereStack(serverWhereBuilder.and());

				break;
			default:
				throw new ParseCancellationException(
						"Expecting an xpath with '" + XWCPSReservedWords.COVERAGE + "' element");
			}

		}

		if (foundCoverage) {
			this.isSimpleXpath = true;
		}

		// FIXME
		return null;

	}

	@Override
	public XpathForClause visitEqualityExpr(EqualityExprContext ctx) {
		if (this.isSimpleXpath) {
			return super.visitEqualityExpr(ctx);
		}

		if (ctx.relationalExpr().size() == 1) {
			return super.visitEqualityExpr(ctx);
		}

		String key = ctx.relationalExpr(0).getText();
		String value = ctx.relationalExpr(1).getText();

		Metadatum metadatum = new Metadatum();
		metadatum.setName(key.replaceAll("@", ""));
		metadatum.setValue(XWCPSEvalUtils.removeQuates(value));
		
		switch (this.currentState) {
		case COVERAGE: {
			this.myCoverages.attribute(new Pair<String, String>(metadatum.getName(), metadatum.getValue()));
			break;
		}
		case SERVER: {
			this.myServers.attribute(new Pair<String, String>(metadatum.getName(), metadatum.getValue()));
			break;
		}
		default:
			break;
		}

		//this.stack.pushWhereBuilderStack(this.stack.peekWhereStack().expression(metadatum));

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

//class XpathForClauseEvalVisitorStack {
//	private Stack<WhereBuilder<Coverage>> whereBuilderStack = new Stack<>();
//	private Stack<Where<Coverage>> whereStack = new Stack<>();
//
//	public void pushWhereBuilderStack(WhereBuilder<Coverage> builder) {
//		whereBuilderStack.push(builder);
//	}
//
//	public boolean isEmptyWhereStack() {
//		return whereStack.isEmpty();
//	}
//
//	public boolean isEmptyWhereBuilderStack() {
//		return whereBuilderStack.isEmpty();
//	}
//
//	public WhereBuilder<Coverage> peekWhereBuilderStack() {
//		return whereBuilderStack.peek();
//	}
//
//	public WhereBuilder<Coverage> popWhereBuilderStack() {
//		WhereBuilder<Coverage> pop = whereBuilderStack.pop();
//		return pop;
//	}
//
//	public void pushWhereStack(Where<Coverage> builder) {
//		whereStack.push(builder);
//	}
//
//	public Where<Coverage> popWhereStack() {
//		Where<Coverage> pop = whereStack.pop();
//		return pop;
//	}
//
//	public Where<Coverage> peekWhereStack() {
//		return whereStack.peek();
//	}
//}
