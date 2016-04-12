package gr.cite.earthserver.wcps.parser.evaluation;

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

public class MixedValue {

	@JsonSerialize(using = InputStreamBase64Serializer.class)
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

class InputStreamBase64Serializer extends JsonSerializer<InputStream> {

	@Override
	public void serialize(InputStream inputStream, JsonGenerator jsonGenerator, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		try(InputStream is = inputStream) {
			jsonGenerator.writeString(Base64.getEncoder().encodeToString(IOUtils.toByteArray(inputStream)));
		}
		
	}

}
