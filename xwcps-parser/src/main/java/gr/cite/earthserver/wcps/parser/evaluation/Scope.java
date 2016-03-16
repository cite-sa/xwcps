package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Scope {
	private Map<String, String> variables = new HashMap<>();

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
		return variables.keySet();
	}

	public String getVariableValue(String variableName) {
		Scope scope = this;

		String value = null;

		while ((scope != null) && ((value = scope.getLocalVariableValue(variableName)) == null)) {
			scope = scope.getParentScope();
		}

		return value;
	}

	private String getLocalVariableValue(String variableName) {
		return variables.get(variableName);
	}

	public void setVariable(String variable, String value) {
		variables.put(variable, value);
	}

	public Scope addScope() {
		Scope scope = new Scope(this);
		scopes.add(scope);
		return scope;
	}

	public Scope getParentScope() {
		return parentScope;
	}
}