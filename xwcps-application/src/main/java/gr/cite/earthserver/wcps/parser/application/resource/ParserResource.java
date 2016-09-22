package gr.cite.earthserver.wcps.parser.application.resource;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.util.JSONPObject;

import gr.cite.earthserver.wcps.parser.XWCPSQueryParser;
import gr.cite.earthserver.wcps.parser.core.XwcpsQueryResult;
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcs.core.Coverage;

@Component
@Path("parser")
@Produces(MediaType.APPLICATION_JSON)
public class ParserResource {

	private XWCPSQueryParser xwcpsQueryParser;

	@Inject
	public ParserResource(XWCPSQueryParser xwcpsQueryParser) {
		this.xwcpsQueryParser = xwcpsQueryParser;
	}

	@GET
	@Path("ping")
	public Response ping() {
		return Response.ok("pong").build();
	}
	
	@GET
	@Path("QueryXwcps")
	//@Produces("multipart/mixed")
	public XwcpsQueryResult query(@QueryParam("q") String query) {
		// return "blakas";

		Query result = xwcpsQueryParser.parse(query);
		
		return result;

		// gr.cite.earthserver.wcps.parser.core.Error error = new
		// gr.cite.earthserver.wcps.parser.core.Error();
		// error.setMessage("gamw pio poli tin java");
		// List<gr.cite.earthserver.wcps.parser.core.Error> tErrors = new
		// ArrayList<>();
		// tErrors.add(error);
		// result.setErrors(tErrors);
		
		//MultiPart multiPart = this.GenerateMultiPartResponse(result);
		//return Response.ok(multiPart).build();
	}
	
	private MultiPart GenerateMultiPartResponse(Query result){
		MultiPart multiPart = new MultiPart();
		multiPart.type(new MediaType("multipart", "mixed"));
		
		List<BodyPart> bodyParts = multiPart.getBodyParts();

		for (Coverage c : result.getCoverageValueMap().keySet()) {
			if (result.getCoverageValueMap().get(c).getWcpsValue() != null) {

				BodyPart bodyPart = new BodyPart(result.getCoverageValueMap().get(c).getWcpsValue(),
						result.getCoverageValueMap().get(c).getWcpsMediaType());

				try {
					ContentDisposition contentDisposition = new ContentDisposition("coverage");
					bodyPart.setContentDisposition(contentDisposition);
				} catch (ParseException e) {
					throw new WebApplicationException();
				}

				bodyParts.add(bodyPart);
			}
			
			if (result.getCoverageValueMap().get(c).getXwcpsValue() != null) {
				BodyPart bodyPart = new BodyPart(result.getCoverageValueMap().get(c).getXwcpsValue(), MediaType.APPLICATION_XML_TYPE);
				try {
					bodyPart.setContentDisposition(new ContentDisposition("aggregation_value"));
				} catch (ParseException e) {
					throw new WebApplicationException();
				}
				bodyParts.add(bodyPart);
			}
			
			break;
		}
		
		try {
			multiPart.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return multiPart;
	}

	@GET
	@Path("query")
	@Produces("multipart/mixed")
	public Response queryWithMixedResponse(@QueryParam("q") String query) {

//		// TODO
//
//		XwcpsQueryResult queryResult = xwcpsQueryParser.parse(query);
//
//		MultiPart multiPart = new MultiPart();
//		List<BodyPart> bodyParts = multiPart.getBodyParts();
//
//		String aggregatedValue = queryResult.getAggregatedValue();
//		if (aggregatedValue != null && !aggregatedValue.isEmpty()) {
//			BodyPart bodyPart = new BodyPart(aggregatedValue, MediaType.APPLICATION_XML_TYPE);
//			try {
//				bodyPart.setContentDisposition(new ContentDisposition("aggregation_value"));
//			} catch (ParseException e) {
//				throw new WebApplicationException();
//			}
//			bodyParts.add(bodyPart);
//		}
//
//		for (MixedValue mixedValue : queryResult.getMixedValues()) {
//
//			// MultiPart coverageMultiPart = new MultiPart();
//
//			BodyPart bodyPart = new BodyPart(mixedValue.getWcpsValue(), mixedValue.getWcpsMediaType());
//
//			// coverageMultiPart.getBodyParts().add(bodyPart);
//
//			try {
//				ContentDisposition contentDisposition = new ContentDisposition("coverage");
//				bodyPart.setContentDisposition(contentDisposition);
//			} catch (ParseException e) {
//				throw new WebApplicationException();
//			}
//
//			bodyParts.add(bodyPart);
//		}
//
//		return Response.ok(multiPart).build();

		return Response.ok("YANNIS").build();
	}

	@GET
	@Path("queryP")
	@Produces("application/x-javascript")
	public JSONPObject query(@QueryParam("callback") String callack, @QueryParam("q") String query) {
		return new JSONPObject(callack, query(query));
	}

}
