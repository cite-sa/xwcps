package gr.cite.earthserver.wcps.parser.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class XwcpsReturnValue {
	@JsonSerialize(using = InputStreamBase64Serializer.class)
	private String wcpsValue;

	private MediaType wcpsMediaType;

	private String subQuery;

	private String xwcpsValue;

	public XwcpsReturnValue() {
	}

	public XwcpsReturnValue(String xwcpsValue) {
		this.xwcpsValue = xwcpsValue;
	}

	public String getWcpsValue() {
		return wcpsValue;
	}

	public void setWcpsValue(String wcpsValue) {
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

	public String getSubQuery() {
		return subQuery;
	}

	public void setSubQuery(String subQuery) {
		this.subQuery = subQuery;
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
		XwcpsReturnValue other = (XwcpsReturnValue) obj;
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

class InputStreamBase64Serializer extends JsonSerializer<InputStream> {

	@Override
	public void serialize(InputStream inputStream, JsonGenerator jsonGenerator, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jsonGenerator.writeString(Base64.getEncoder().encodeToString(IOUtils.toByteArray(inputStream)));

		if (inputStream instanceof ByteArrayInputStream) {
			// TODO remove temporal
			inputStream.reset();
		}

		inputStream.close();
	}

}
