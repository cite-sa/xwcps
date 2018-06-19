package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Scope {
	private Map<String, Query> variables = new HashMap<>();

	private List<Scope> scopes = new ArrayList<>();

	private final Scope parentScope;

	public static Scope newRootScope() {
		return new Scope();
	}

	private Scope(Scope parentScope) {
		this.parentScope = parentScope;
	}

	private Scope() {
		this.parentScope = null;
	}

	public Set<String> getVariables() {
		return this.variables.keySet();
	}

	public Query getVariableValue(String variableName) {
		Scope scope = this;

		Query value = null;

		while ((scope != null) && ((value = scope.getLocalVariableValue(variableName)) == null)) {
			scope = scope.getParentScope();
		}

		return value;
	}

	private Query getLocalVariableValue(String variableName) {
		return this.variables.get(variableName);
	}

	public void setVariable(String variable, Query value) {
		this.variables.put(variable, value);
	}

	public Scope addScope() {
		Scope scope = new Scope(this);
		this.scopes.add(scope);
		return scope;
	}

	public Scope getParentScope() {
		return this.parentScope;
	}
}