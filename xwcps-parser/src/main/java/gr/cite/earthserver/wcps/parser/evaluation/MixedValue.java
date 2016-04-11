package gr.cite.earthserver.wcps.parser.evaluation;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

public class MixedValue {

	private InputStream wcpsValue;

	private MediaType wcpsMediaType;

	private String xwcpsValue;

	public InputStream getWcpsValue() {
		return wcpsValue;
	}

	public void setWcpsValue(InputStream wcpsValue) {
		this.wcpsValue = wcpsValue;
	}

	public MediaType getWcpsMediaType() {
		return wcpsMediaType;
	}

	public void setWcpsMediaType(MediaType wcpsMediaType) {
		this.wcpsMediaType = wcpsMediaType;
	}

	public String getXwcpsValue() {
		return xwcpsValue;
	}

	public void setXwcpsValue(String xwcpsValue) {
		this.xwcpsValue = xwcpsValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((wcpsMediaType == null) ? 0 : wcpsMediaType.hashCode());
		result = prime * result + ((wcpsValue == null) ? 0 : wcpsValue.hashCode());
		result = prime * result + ((xwcpsValue == null) ? 0 : xwcpsValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MixedValue other = (MixedValue) obj;
		if (wcpsMediaType == null) {
			if (other.wcpsMediaType != null)
				return false;
		} else if (!wcpsMediaType.equals(other.wcpsMediaType))
			return false;
		if (wcpsValue == null) {
			if (other.wcpsValue != null)
				return false;
		} else if (!wcpsValue.equals(other.wcpsValue))
			return false;
		if (xwcpsValue == null) {
			if (other.xwcpsValue != null)
				return false;
		} else if (!xwcpsValue.equals(other.xwcpsValue))
			return false;
		return true;
	}

}
