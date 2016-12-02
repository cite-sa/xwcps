package gr.cite.earthserver.wcps.parser.application.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Result {
	
	@JsonProperty
	private String coverageId;
	
	@JsonProperty
	private String wcpsValue;
	
	@JsonProperty
	private String xwcpsValue;
	

	public String getCoverageId() {
		return coverageId;
	}

	public void setCoverageId(String coverageId) {
		this.coverageId = coverageId;
	}

	public String getWcpsValue() {
		return wcpsValue;
	}

	public void setWcpsValue(String wcpsValue) {
		this.wcpsValue = wcpsValue;
	}

	public String getXwcpsValue() {
		return xwcpsValue;
	}

	public void setXwcpsValue(String xwcpsValue) {
		this.xwcpsValue = xwcpsValue;
	}
	
	

}
