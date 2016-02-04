package gr.cite.earthserver.wcps.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.wcs.client.WCSRequest;
import gr.cite.earthserver.wcs.client.WCSRequestException;

@Deprecated
public class WCPSClient {
	private static final Logger logger = LoggerFactory.getLogger(WCPSClient.class);

	private String WCSEndpoint;

	public WCPSClient(String WCSEndpoint) {
		super();
		this.WCSEndpoint = WCSEndpoint;
	}

	public String query(String query) throws WCSRequestException {
		logger.info("query: " + query);

		return WCSRequest.newBuilder().endpoint(WCSEndpoint).processCoverages().query(query).build().get();
	}

}
