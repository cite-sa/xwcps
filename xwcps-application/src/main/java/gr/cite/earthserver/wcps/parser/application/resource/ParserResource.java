package gr.cite.earthserver.wcps.parser.application.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.util.JSONPObject;

import gr.cite.earthserver.wcps.parser.XWCPSQueryParser;
import gr.cite.earthserver.wcps.parser.evaluation.Query;

@Path("parser")
@Produces(MediaType.APPLICATION_JSON)
public class ParserResource {
	private static final String WCS_ENDPOINT = "http://flanche.com:9090/rasdaman/ows";

	private XWCPSQueryParser xwcpsQueryParser;

	public ParserResource(XWCPSQueryParser xwcpsQueryParser) {
		this.xwcpsQueryParser = xwcpsQueryParser;
	}

	@GET
	@Path("query")
	public Query query(@QueryParam("q") String query) {
		return xwcpsQueryParser.parse(query);
	}

	@GET
	@Path("queryP")
	public JSONPObject query(@QueryParam("callback") String callack, @QueryParam("q") String query) {
		return new JSONPObject(callack, query(query));
	}

	@GET
	@Path("evaluate")
	@Produces(MediaType.APPLICATION_XML)
	public String evaluate(@QueryParam("q") String query) {
		return xwcpsQueryParser.parse(query).getValue();
	}

}
