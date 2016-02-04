package gr.cite.earthserver.wcps.parser.errors;

public enum SemanticError {
	XML_ELEMENTS("XML elements must open and close with the same tag."),
	VARIABLE_NOT_DEFINED("Variable in return clause must be defined in for clause.");
	
	private String message;
	
	private SemanticError(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return message;
	}
	
	
}
