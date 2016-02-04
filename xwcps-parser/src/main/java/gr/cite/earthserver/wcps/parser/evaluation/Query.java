package gr.cite.earthserver.wcps.parser.evaluation;

public class Query {
	private String query;

	private boolean evaluated = false;

	private String value;

	private String error; // TODO list??

	private boolean isSimpleWCPS;

	public Query setQuery(String wcpsQuery) {
		this.query = wcpsQuery;
		return this;
	}

	public String getQuery() {
		return query;
	}

	public Query appendQuery(String query) {
		this.query += query;
		return this;
	}

	public boolean isEvaluated() {
		return evaluated;
	}

	protected Query evaluated() {
		setEvaluated(true);
		return this;
	}

	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}

	public Query setValue(String value) {
		this.value = value;
		return this;
	}
	
	public Query prependValue(String prependValue) {
		this.value = prependValue + this.value;
		return this;
	}
	
	public Query appendValue(String prependValue) {
		this.value += prependValue;
		return this;
	}

	public String getValue() {
		return value;
	}

	public String getError() {
		return error;
	}

	public Query setError(String error) {
		this.error = error;
		return this;
	}

	public boolean hasError() {
		return this.error != null;
	}

	public Query aggregate(Query nextResult, boolean overrideValue) {
		// FIXME aggregations

		query = query + " " + nextResult.getQuery();

		if (value == null || overrideValue) {
			value = nextResult.getValue();
		} else {
			value = (value == null ? "" : value) + (nextResult.getValue() == null ? "" : nextResult.getValue());
		}

		if (error == null) {
			error = nextResult.getError();
		}

		if (!evaluated) {
			evaluated = nextResult.isEvaluated();
		}
		return this;
	}
	
	public Query aggregate(Query nextResult) {
		return aggregate(nextResult, false);
	}

	@Override
	public String toString() {
		return query;
	}

	public Query prependQuery(String prependedQuery) {
		this.query = prependedQuery + this.query;
		return this;
	}

	public Query simpleWCPS() {
		this.isSimpleWCPS = true;
		return this;
	}
	
	public boolean isSimpleWCPS() {
		return isSimpleWCPS;
	}
	
}
