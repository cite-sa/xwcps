package gr.cite.earthserver.wcs.client;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WCSRequest {
	private static final Logger logger = LoggerFactory.getLogger(WCSRequest.class);

	public static WCSRequestBuilder newBuilder() {
		return new WCSRequestBuilder();
	}

	private WebTarget webTarget;

	WCSRequest(WebTarget webTarget) {
		this.webTarget = webTarget;
	}

	public String get() throws WCSRequestException {
		// Response response =
		// this.webTarget.request(MediaType.APPLICATION_XML).get();
		Response response = this.webTarget.request(MediaType.TEXT_PLAIN).get();

		// try (InputStream stream = (InputStream) response.getEntity()) {
		//
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
		// MultiPart multipart = r.readEntity(MultiPart.class);
		// System.out.println(multipart.getBodyParts().stream().map(part ->
		// part.getEntityAs(String.class))
		// .collect(Collectors.toList()));
		

		return responseString;

	}

	public static void main(String[] args) throws WCSRequestException {
		String NIR = WCSRequest.newBuilder().endpoint("http://flanche.com:9090/rasdaman/ows").describeCoverage()
				.coverageId("NIR").build().get();

		System.out.println(NIR);
	}
}
