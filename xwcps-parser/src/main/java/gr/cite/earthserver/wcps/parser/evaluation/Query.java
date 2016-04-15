package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.parser.core.Error;
import gr.cite.earthserver.wcps.parser.core.MixedValue;
import gr.cite.earthserver.wcps.parser.core.XwcpsQueryResult;

public class Query extends XwcpsQueryResult {
	private String query;

	private List<Query> splittedQuery = new ArrayList<>();

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

	public Query evaluated() {
		setEvaluated(true);
		return this;
	}

	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}

	public Query setValue(String value) {
		setAggregatedValue(value);
		return this;
	}

	public Query prependValue(String prependValue) {
		setAggregatedValue(prependValue + getAggregatedValue());
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

	public List<Query> getSplittedQuery() {
		return splittedQuery;
	}

	public Query setSplittedQuery(List<Query> splittedQuery) {
		this.splittedQuery = splittedQuery;
		return this;
	}

	public Query aggregate(Query nextResult, boolean overrideValue) {

		query = aggreagateQuery(query, nextResult);

		for (Query splitted : splittedQuery) {
			splitted.setAggregatedValue(aggreagateQuery(splitted.getAggregatedValue(), nextResult));
		}
		if (splittedQuery.isEmpty() && !nextResult.getSplittedQuery().isEmpty()) {
			// for (String splitted : nextResult.getSplittedQuery()) {
			// splittedQuery.add(query + " " + splitted);
			// }
			//
			// FIXME the afforementioned is the correct implementation,
			// the following is temporal
			splittedQuery = nextResult.getSplittedQuery();
		}

		if (aggregatedValue == null || overrideValue) {
			aggregatedValue = nextResult.serializeValue();
		} else {
			aggregatedValue = serializeValue() + nextResult.serializeValue();
		}

		if (coverageValueMap != null && nextResult.getCoverageValueMap() != null) {
			for (Entry<Coverage, String> coverageEntry : nextResult.getCoverageValueMap().entrySet()) {
				if (coverageEntry.getValue() != null) {
					if (coverageValueMap.containsKey(coverageEntry.getKey())) {
						String currentValue = coverageValueMap.get(coverageEntry.getKey());
						
						coverageValueMap.put(coverageEntry.getKey(), currentValue + coverageEntry.getValue());
					} else {
						coverageValueMap.put(coverageEntry.getKey(), coverageEntry.getValue());
					}
				}
			}
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

	private static String aggreagateQuery(String query, Query nextResult) {
		return query == null ? nextResult.getQuery() : query + " " + nextResult.getQuery();
	}

	public String serializeValue() {
		if (coverageValueMap != null) {
			return coverageValueMap.values().stream().filter(v -> v != null).collect(Collectors.joining());
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
