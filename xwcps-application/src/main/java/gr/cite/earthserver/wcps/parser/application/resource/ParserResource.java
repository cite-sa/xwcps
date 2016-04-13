package gr.cite.earthserver.wcps.parser.application.resource;

import java.text.ParseException;
import java.util.List;

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

import com.fasterxml.jackson.databind.util.JSONPObject;

import gr.cite.earthserver.wcps.parser.XWCPSQueryParser;
import gr.cite.earthserver.wcps.parser.core.MixedValue;
import gr.cite.earthserver.wcps.parser.core.XwcpsQueryResult;
import gr.cite.earthserver.wcps.parser.evaluation.Query;

@Path("parser")
@Produces(MediaType.APPLICATION_JSON)
public class ParserResource {

	private XWCPSQueryParser xwcpsQueryParser;

	public ParserResource(XWCPSQueryParser xwcpsQueryParser) {
		this.xwcpsQueryParser = xwcpsQueryParser;
	}

	@GET
	@Path("query")
	public XwcpsQueryResult query(@QueryParam("q") String query) {
		return xwcpsQueryParser.parse(query);
	}

	@GET
	@Path("query")
	@Produces("multipart/mixed")
	public Response queryWithMixedResponse(@QueryParam("q") String query) {

		// TODO 
		
		XwcpsQueryResult queryResult = xwcpsQueryParser.parse(query);

		MultiPart multiPart = new MultiPart();
		List<BodyPart> bodyParts = multiPart.getBodyParts();

		String aggregatedValue = queryResult.getAggregatedValue();
		if (aggregatedValue != null && !aggregatedValue.isEmpty()) {
			BodyPart bodyPart = new BodyPart(aggregatedValue, MediaType.APPLICATION_XML_TYPE);
			try {
				bodyPart.setContentDisposition(new ContentDisposition("aggregation_value"));
			} catch (ParseException e) {
				throw new WebApplicationException();
			}
			bodyParts.add(bodyPart);
		}

		for (MixedValue mixedValue : queryResult.getMixedValues()) {
			
//			MultiPart coverageMultiPart = new MultiPart();
			
			BodyPart bodyPart = new BodyPart(mixedValue.getWcpsValue(), mixedValue.getWcpsMediaType());

//			coverageMultiPart.getBodyParts().add(bodyPart);

			try {
				ContentDisposition contentDisposition = new ContentDisposition("coverage");
				bodyPart.setContentDisposition(contentDisposition);
			} catch (ParseException e) {
				throw new WebApplicationException();
			}
			
			bodyParts.add(bodyPart);
		}

		return Response.ok(multiPart).build();
	}

	@GET
	@Path("queryP")
	@Produces("application/x-javascript")
	public JSONPObject query(@QueryParam("callback") String callack, @QueryParam("q") String query) {
		return new JSONPObject(callack, query(query));
	}

}
