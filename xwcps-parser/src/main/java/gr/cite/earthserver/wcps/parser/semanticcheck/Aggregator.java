package gr.cite.earthserver.wcps.parser.semanticcheck;

import java.util.HashSet;
import java.util.Set;

import gr.cite.earthserver.wcps.grammar.XWCPSParser.CoverageVariableNameLabelContext;

public class Aggregator {
	Set<CoverageVariableNameLabelContext> variables;
	
	public Aggregator() {
		variables = new HashSet<>();
	}

	public Set<CoverageVariableNameLabelContext> getVariables() {
		return variables;
	}

	public void setVariables(Set<CoverageVariableNameLabelContext> variables) {
		this.variables = variables;
	}
	
	public Aggregator add(CoverageVariableNameLabelContext variable) {
		variables.add(variable);
		return this;
	}
	public Aggregator addAll(Aggregator aggregator) {
		variables.addAll(aggregator.getVariables());
		return this;
	}
	
	public boolean containsAll(Aggregator aggregator) {
		return variables.containsAll(aggregator.getVariables());
	}
}
