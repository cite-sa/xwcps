package gr.cite.earthserver.wcps.parser.core;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonInclude(content = Include.NON_NULL)
public class XwcpsQueryResult {

	@JsonProperty
	protected List<Error> errors;

//	@JsonProperty
//	protected Set<XwcpsReturnValue> mixedValues;
	
	@JsonProperty
	protected String aggregatedValue;

//	public Set<XwcpsReturnValue> getMixedValues() {
//		return mixedValues;
//	}
//
//	public void setMixedValues(Set<XwcpsReturnValue> mixedValues) {
//		this.mixedValues = mixedValues;
//	}
	
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
