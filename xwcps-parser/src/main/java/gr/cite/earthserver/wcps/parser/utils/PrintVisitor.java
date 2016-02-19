package gr.cite.earthserver.wcps.parser.utils;

import org.antlr.v4.runtime.tree.TerminalNode;

import gr.cite.earthserver.wcps.grammar.XWCPSBaseVisitor;

public class PrintVisitor extends XWCPSBaseVisitor<StringBuilder> {

	@Override
	public StringBuilder visitTerminal(TerminalNode node) {
		return new StringBuilder(node.getText());
	}
	
	@Override
	protected StringBuilder aggregateResult(StringBuilder aggregate, StringBuilder nextResult) {
		if (nextResult == null) {
			return aggregate;
		} else if (aggregate == null) {
			return nextResult;
		}
		return aggregate.append(" ").append(nextResult);
	}
	
}
