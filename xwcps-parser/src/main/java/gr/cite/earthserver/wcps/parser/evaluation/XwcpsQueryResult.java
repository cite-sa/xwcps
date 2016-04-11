package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(content = Include.NON_NULL)
public class XwcpsQueryResult {

	@JsonProperty
	List<Error> errors;

	@JsonProperty
	Set<MixedValue> mixedValues;

	@JsonProperty
	String aggregatedValue;

	public Set<MixedValue> getMixedValues() {
		return mixedValues;
	}

	public void setMixedValues(Set<MixedValue> mixedValues) {
		this.mixedValues = mixedValues;
	}

	public String getAggregatedValue() {
		return aggregatedValue;
	}

	public void setAggregatedValue(String aggregatedValue) {
		this.aggregatedValue = aggregatedValue;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

}
