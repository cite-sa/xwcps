package gr.cite.earthserver.wcps.parser.application;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.core.JsonGenerator;

import gr.cite.earthserver.wcps.parser.application.resource.ParserResource;

@ApplicationPath("restAPI")
public class XWCPSParserApplication extends ResourceConfig {
	
	public XWCPSParserApplication() {
		register(CORSFilter.class);
		register(JacksonFeature.class);
		register(MultiPartFeature.class);
//		register(JsonProcessingFeature.class);
//        packages("org.glassfish.jersey.examples.jsonp");
		register(ParserResource.class);
		
	}
	
}