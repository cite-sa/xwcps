package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import gr.cite.earthserver.metadata.core.Coverage;

public class Query extends XwcpsQueryResult {
	private String query;

	private boolean evaluated = false;

	private boolean isSimpleWCPS;

	private Map<Coverage, String> coverageValueMap;

	public Query() {
		setMixedValues(new HashSet<>());
		setErrors(new ArrayList<>());
	}

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
		this.aggregatedValue = value;
		return this;
	}

	public Query prependValue(String prependValue) {
		this.aggregatedValue = prependValue + this.aggregatedValue;
		return this;
	}

	public Query addMixedValue(MixedValue mixedValue) {
		getMixedValues().add(mixedValue);
		return this;
	}

	public Query appendValue(String prependValue) {
		this.aggregatedValue += prependValue;
		return this;
	}

	public String getValue() {
		return aggregatedValue;
	}

	public Query setError(String error) {
		getErrors().add(new Error() {
			{
				setMessage(error);
			}
		});
		return this;
	}

	public boolean hasError() {
		return getErrors().size() > 0;
	}

	public Query aggregate(Query nextResult, boolean overrideValue) {
		// FIXME aggregations

		query = (query == null ? nextResult.getQuery() : query + " " + nextResult.getQuery());

		if (aggregatedValue == null || overrideValue) {
			aggregatedValue = nextResult.serializeValue();
		} else {
			aggregatedValue = serializeValue() + nextResult.serializeValue();
		}

		if (coverageValueMap != null && nextResult.getCoverageValueMap() != null) {
			coverageValueMap.putAll(nextResult.getCoverageValueMap());
		} else if (nextResult.getCoverageValueMap() != null) {
			coverageValueMap = nextResult.getCoverageValueMap();
		}

		getMixedValues().addAll(nextResult.getMixedValues());

		getErrors().addAll(nextResult.getErrors());

		if (!evaluated) {
			evaluated = nextResult.isEvaluated();
		}
		return this;
	}

	public String serializeValue() {
		if (coverageValueMap != null) {
			return coverageValueMap.values().stream().collect(Collectors.joining());
		} else if (aggregatedValue != null) {
			return aggregatedValue;
		} else {
			return "";
		}
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

	public Map<Coverage, String> getCoverageValueMap() {
		return coverageValueMap;
	}

	public Query setCoverageValueMap(Map<Coverage, String> coverageValueMap) {
		this.coverageValueMap = coverageValueMap;
		return this;
	}
}
