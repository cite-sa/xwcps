package gr.cite.earthserver.wcps.parser.evaluation.visitors;

import java.util.HashSet;
import java.util.Set;

import gr.cite.earthserver.wcps.grammar.XWCPSBaseVisitor;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.CoverageVariableNameLabelContext;

public class GetVariableVisitor extends XWCPSBaseVisitor<Void> {

	private Set<String> variables = new HashSet<>();

	@Override
	public Void visitCoverageVariableNameLabel(CoverageVariableNameLabelContext ctx) {

		variables.add(ctx.getText());

		return null;
	}

	public Set<String> getVariables() {
		return variables;
	}
}
