package gr.cite.earthserver.wcs.client;

import java.io.InputStream;
import java.util.HashSet;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.wcps.parser.evaluation.MixedValue;
import gr.cite.earthserver.wcps.parser.evaluation.XwcpsQueryResult;

public class WCSRequest {
	private static final Logger logger = LoggerFactory.getLogger(WCSRequest.class);

	public static WCSRequestBuilder newBuilder() {
		return new WCSRequestBuilder();
	}

	private WebTarget webTarget;

	WCSRequest(WebTarget webTarget) {
		this.webTarget = webTarget;
	}

	public XwcpsQueryResult get() throws WCSRequestException {
		// Response response =
		// this.webTarget.request(MediaType.APPLICATION_XML).get();
		Response response = this.webTarget.request(MediaType.TEXT_PLAIN).get();

		XwcpsQueryResult xwcpsQueryResult = new XwcpsQueryResult();

		MediaType mediaType = MediaType.valueOf(response.getHeaderString("Content-Type"));

		if (mediaType.toString().contains("image")) {
			xwcpsQueryResult.setMixedValues(new HashSet<>());

			MixedValue image = new MixedValue();
			image.setWcpsMediaType(mediaType);
			image.setWcpsValue((InputStream) response.getEntity());
			xwcpsQueryResult.getMixedValues().add(image);

		} else {
			String responseString = response.readEntity(String.class);

			if (response.getStatus() >= 300) {
				throw new WCSRequestException(responseString, response.getStatus());
			}

			// TODO Content-type: multipart/x-mixed-replace;boundary=End
			logger.warn("-----------------------------------------------------------------------");
			logger.warn("-----------------------------------------------------------------------");
			logger.warn("--  TODO  read Content-type: multipart/x-mixed-replace;boundary=End  --");
			logger.warn("-----------------------------------------------------------------------");
			logger.warn("-----------------------------------------------------------------------");
			// FIXME delete if
			if (responseString.startsWith("\r\n--End")) {
				responseString = responseString.replaceAll("--End--", "").replaceAll("--End", "")
						.replaceAll("Content-type: text/plain", "").trim();
			}

			xwcpsQueryResult.setAggregatedValue(responseString);
		}

		return xwcpsQueryResult;

	}

}
