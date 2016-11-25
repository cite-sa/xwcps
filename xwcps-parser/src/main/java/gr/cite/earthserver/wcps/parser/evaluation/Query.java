package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

//import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.parser.core.Error;
import gr.cite.earthserver.wcps.parser.core.XwcpsQueryResult;
import gr.cite.earthserver.wcps.parser.core.XwcpsReturnValue;
import gr.cite.earthserver.wcs.core.Coverage;

public class Query extends XwcpsQueryResult {
	private String query;

	private List<Query> splittedQuery = new ArrayList<>();

	private boolean evaluated = false;

	private boolean isSimpleWCPS;

	private Map<Coverage, XwcpsReturnValue> coverageValueMap;

	public Query() {
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
		return this.evaluated;
	}

	public Query evaluated() {
		this.setEvaluated(true);
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

	public Query appendValue(String prependValue) {
		this.aggregatedValue += prependValue;
		return this;
	}

	public String getValue() {
		return "<results>" + aggregatedValue + "</results>";
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

		this.query = aggreagateQuery(this.query, nextResult);

		for (Query splitted : this.splittedQuery) {
			splitted.setAggregatedValue(aggreagateQuery(splitted.getAggregatedValue(), nextResult));
		}
		if (this.splittedQuery.isEmpty() && !nextResult.getSplittedQuery().isEmpty()) {
			// for (String splitted : nextResult.getSplittedQuery()) {
			// splittedQuery.add(query + " " + splitted);
			// }
			//
			// FIXME the afforementioned is the correct implementation,
			// the following is temporal
			this.splittedQuery = nextResult.getSplittedQuery();
		}

		if (this.aggregatedValue == null || overrideValue) {
			this.aggregatedValue = nextResult.serializeValue();
		} else {
			this.aggregatedValue = serializeValue() + nextResult.serializeValue();
		}

		if (!nextResult.getCoverageValueMap().isEmpty()) {
			if (this.getCoverageValueMap().isEmpty()) {
				this.getCoverageValueMap().putAll(nextResult.getCoverageValueMap());
			} else {
				for (Entry<Coverage, XwcpsReturnValue> coverageEntry : nextResult.getCoverageValueMap().entrySet()) {
					if (coverageEntry.getValue() == null)
						continue;

					if (this.getCoverageValueMap().containsKey(coverageEntry.getKey())) {
						XwcpsReturnValue currentValue = this.getCoverageValueMap().get(coverageEntry.getKey());

						if (currentValue == null)
							this.getCoverageValueMap().put(coverageEntry.getKey(), coverageEntry.getValue());
						else {
							XwcpsReturnValue returnValue = new XwcpsReturnValue();
							
							StringBuilder stringBuilder = new StringBuilder();
							if (currentValue.getXwcpsValue() != null) stringBuilder.append(currentValue.getXwcpsValue());
							if (coverageEntry.getValue().getXwcpsValue() != null) stringBuilder.append(coverageEntry.getValue().getXwcpsValue());
							returnValue.setXwcpsValue(stringBuilder.toString());
							
							this.getCoverageValueMap().put(coverageEntry.getKey(), returnValue);
						}
					} else {
						this.getCoverageValueMap().put(coverageEntry.getKey(), coverageEntry.getValue());
					}
				}
			}
		}

//		getMixedValues().addAll(nextResult.getMixedValues());

		getErrors().addAll(nextResult.getErrors());

		if (!this.evaluated) {
			this.evaluated = nextResult.isEvaluated();
		}
		return this;
	}
	
	public Query aggregateReturn(Query nextResult, boolean overrideValue) {

		this.query = aggreagateQuery(this.query, nextResult);

		for (Query splitted : splittedQuery) {
			splitted.setAggregatedValue(aggreagateQuery(splitted.getAggregatedValue(), nextResult));
		}
		if (this.splittedQuery.isEmpty() && !nextResult.getSplittedQuery().isEmpty()) {
			// for (String splitted : nextResult.getSplittedQuery()) {
			// splittedQuery.add(query + " " + splitted);
			// }
			//
			// FIXME the afforementioned is the correct implementation,
			// the following is temporal
			this.splittedQuery = nextResult.getSplittedQuery();
		}

		if (this.aggregatedValue == null || overrideValue) {
			this.aggregatedValue = nextResult.serializeValue();
		} else {
			this.aggregatedValue = serializeValue() + nextResult.serializeValue();
		}

		if (!nextResult.getCoverageValueMap().isEmpty()) {
			if (this.getCoverageValueMap().isEmpty()) {
				this.getCoverageValueMap().putAll(nextResult.getCoverageValueMap());
			} else {
				for (Entry<Coverage, XwcpsReturnValue> coverageEntry : nextResult.getCoverageValueMap().entrySet()) {
					if (coverageEntry.getValue() == null)
						continue;

					if (this.getCoverageValueMap().containsKey(coverageEntry.getKey())) {
						XwcpsReturnValue currentValue = this.getCoverageValueMap().get(coverageEntry.getKey());

						if (currentValue == null)
							this.getCoverageValueMap().put(coverageEntry.getKey(), coverageEntry.getValue());
						else if (coverageEntry.getValue().getXwcpsValue() == null)
							continue;
						else {
							XwcpsReturnValue returnValue = new XwcpsReturnValue();
							returnValue.setXwcpsValue(
									currentValue.getXwcpsValue() + coverageEntry.getValue().getXwcpsValue());
							this.getCoverageValueMap().put(coverageEntry.getKey(), returnValue);
						}
					} else {
						this.getCoverageValueMap().put(coverageEntry.getKey(), coverageEntry.getValue());
					}
				}
			}
		}

//		getMixedValues().addAll(nextResult.getMixedValues());

		getErrors().addAll(nextResult.getErrors());

		if (!this.evaluated) {
			this.evaluated = nextResult.isEvaluated();
		}
		return this;
	}

	private static String aggreagateQuery(String query, Query nextResult) {
		return query == null ? nextResult.getQuery() : query + " " + nextResult.getQuery();
	}

	public String serializeValue() {
		if (!this.getCoverageValueMap().isEmpty()) {
			return this.getCoverageValueMap().values().stream().filter(v -> v.getXwcpsValue() != null).map(x -> x.getXwcpsValue())
					.collect(Collectors.joining());
		} else if (this.aggregatedValue != null) {
			return this.aggregatedValue;
		} else {
			return "";
		}
	}

	public Query aggregate(Query nextResult) {
		return aggregate(nextResult, false);
	}

	@Override
	public String toString() {
		return this.query;
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
		return this.isSimpleWCPS;
	}

	public Map<Coverage, XwcpsReturnValue> getCoverageValueMap() {
		if (this.coverageValueMap == null)
			this.coverageValueMap = new HashMap<Coverage, XwcpsReturnValue>();
		return this.coverageValueMap;
	}

	// public Query setCoverageValueMap(Map<Coverage, XwcpsReturnValue>
	// coverageValueMap) {
	// this.coverageValueMap = coverageValueMap;
	// return this;
	// }
}
