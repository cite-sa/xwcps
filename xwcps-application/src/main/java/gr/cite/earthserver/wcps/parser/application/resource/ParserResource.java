package gr.cite.earthserver.wcps.parser.application.resource;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import gr.cite.earthserver.wcps.parser.XWCPSQueryParser;
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcs.core.Coverage;

@Component
@Path("parser")
@Produces(MediaType.APPLICATION_JSON)
public class ParserResource {
	private static  final Logger logger = LoggerFactory.getLogger(ParserResource.class);

	private XWCPSQueryParser xwcpsQueryParser;

	@Inject
	public ParserResource(XWCPSQueryParser xwcpsQueryParser) {
		this.xwcpsQueryParser = xwcpsQueryParser;
	}

	@GET
	@Path("ping")
	@Produces(MediaType.TEXT_PLAIN)
	public Response ping() {
		logger.info("ping-pong");
		return Response.ok("pong").build();
	}
	
	@GET
	@Path("queryXwcps")
	@Produces("multipart/mixed")
	public Response query(@QueryParam("q") String query) {
		try {
			MultiPart multiPart = null;
			Query result = xwcpsQueryParser.parse(query);
			try {
				multiPart = this.generateMultiPartResponse(result);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return Response.ok(multiPart).build();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new WebApplicationException(e.getMessage(), e);
		}

	}
	
	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryJson(@QueryParam("q") String query) {
		try {
			List<Result> results = new ArrayList<>();
			Query result = xwcpsQueryParser.parse(query);
	
			for (Coverage orderedCoverage : result.getOrderedCoverages()) {
				Result newResult = new Result();
				
				newResult.setCoverageId(orderedCoverage.getCoverageId());
				if (result.getCoverageValueMap().get(orderedCoverage).getWcpsValue() != null) {
					newResult.setWcpsValue(result.getCoverageValueMap().get(orderedCoverage).getWcpsValue());
				}
				
				if (result.getCoverageValueMap().get(orderedCoverage).getXwcpsValue() != null) {
					newResult.setXwcpsValue(result.getCoverageValueMap().get(orderedCoverage).getXwcpsValue());
				}
				
				results.add(newResult);
			}
			
			//Add all coverages that didn't take part to the ordering - maybe xpath didn't exist - at the end of the list
			for (Coverage c : result.getCoverageValueMap().keySet()) {
				if (result.getOrderedCoverages().contains(c)) continue;
				
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
			
	//		for (Coverage c : result.getCoverageValueMap().keySet()) {
	//			Result newResult = new Result();
	//
	//			newResult.setCoverageId(c.getCoverageId());
	//			if (result.getCoverageValueMap().get(c).getWcpsValue() != null) {
	//				newResult.setWcpsValue(result.getCoverageValueMap().get(c).getWcpsValue());
	//			}
	//
	//			if (result.getCoverageValueMap().get(c).getXwcpsValue() != null) {
	//				newResult.setXwcpsValue(result.getCoverageValueMap().get(c).getXwcpsValue());
	//			}
	//
	//			results.add(newResult);
	//		}
			
			return Response.ok(results).build();
		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new WebApplicationException(e.getMessage(), e);
		}
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
			logger.error(e.getMessage(), e);
		}
		
		return multiPart;
	}

}
