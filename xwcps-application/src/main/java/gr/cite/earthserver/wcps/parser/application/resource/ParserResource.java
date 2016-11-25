package gr.cite.earthserver.wcps.parser.application.resource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;

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
	@Path("queryXwcps")
	@Produces("multipart/mixed")
	public Response query(@QueryParam("q") String query) {
		Query result = xwcpsQueryParser.parse(query);
		
		
		MultiPart multiPart = null;
		try {
			multiPart = this.generateMultiPartResponse(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok(multiPart).build();
		
		//return result;
	}
	
	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryJson(@QueryParam("q") String query) {
		Query result = xwcpsQueryParser.parse(query);
		
		List<Result> results = new ArrayList<>();
		
		for (Coverage c : result.getCoverageValueMap().keySet()) {
			Result newResult = new Result();
			
			newResult.setCoverageId(c.getCoverageId());
			if (result.getCoverageValueMap().get(c).getWcpsValue() != null) {
				newResult.setWcpsValue(result.getCoverageValueMap().get(c).getWcpsValue());
			}
			
			if (result.getCoverageValueMap().get(c).getXwcpsValue() != null) {
				newResult.setXwcpsValue(result.getCoverageValueMap().get(c).getXwcpsValue());
			}
			
			results.add(newResult);
		}
		
		return Response.ok(results).build();
		
	}
	
	private MyMultipart generateMultiPartResponse(Query result) throws IOException{
		MyMultipart multiPart = new MyMultipart();
		multiPart.type(new MediaType("multipart", "mixed"));
		
		List<BodyPart> bodyParts = multiPart.getBodyParts();

		for (Coverage c : result.getCoverageValueMap().keySet()) {
			if (result.getCoverageValueMap().get(c).getWcpsValue() != null) {
//				String image = "data:image/png;base64," +
//						DatatypeConverter.printBase64Binary(IOUtils.toByteArray(result.getCoverageValueMap().get(c).getWcpsValue()));
				String image = "data:image/png;base64," + Base64.getEncoder().encodeToString(result.getCoverageValueMap().get(c).getWcpsValue().getBytes());
				//System.out.println(image);
				BodyPart bodyPart = new BodyPart(image,
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

	/*@GET
	@Path("queryP")
	@JSONP("callback")
	@Produces({"application/javascript", "application/x-javascript"})
	public JSONPObject query(@QueryParam("callback") String callack, @QueryParam("q") String query) {
		Response response = query(query);
		return new JSONPObject(callack, response);
	}*/
	
	

}
