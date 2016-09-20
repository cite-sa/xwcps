package gr.cite.earthserver.wcps.parser.application;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import gr.cite.earthserver.wcps.parser.application.resource.ParserResource;

@ApplicationPath("restAPI")
public class XWCPSParserApplication extends ResourceConfig {
	
	public XWCPSParserApplication() {
		register(JacksonFeature.class);
		register(MultiPartFeature.class);
		register(ParserResource.class);
	}
	
}