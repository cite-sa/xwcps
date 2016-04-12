package gr.cite.earthserver.wcps.parser.evaluation.visitors;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.wcps.grammar.XWCPSBaseVisitor;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XwcpsContext;
import gr.cite.earthserver.wcps.parser.evaluation.Query;

public abstract class XWCPSParseTreeVisitor extends XWCPSBaseVisitor<Query> {
	private static final Logger logger = LoggerFactory.getLogger(XWCPSParseTreeVisitor.class);

	@Override
	public Query visitTerminal(TerminalNode node) {
		return new Query().setQuery(node.getText());
	}

	@Override
	protected Query aggregateResult(Query aggregate, Query nextResult) {
		if (aggregate == null) {
			return nextResult;
		}
		return aggregate.aggregate(nextResult);
	}

	@Override
	public abstract Query visitXwcps(XwcpsContext ctx);

}
