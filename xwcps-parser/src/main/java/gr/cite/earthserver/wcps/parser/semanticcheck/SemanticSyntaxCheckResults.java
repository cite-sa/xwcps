package gr.cite.earthserver.wcps.parser.semanticcheck;

import java.util.List;

import gr.cite.earthserver.wcps.parser.errors.SemanticError;

public class SemanticSyntaxCheckResults {
	int errorsNum;
	private List<SemanticError> errors;
	
	public SemanticSyntaxCheckResults() {
		errorsNum = 0;
	}
	
	public List<SemanticError> getErrors() {
		return getErrors();
	}
	
	public void addError(SemanticError error) {
		errors.add(error);
		errorsNum ++;
	}
}
