package gr.cite.earthserver.wcps.parser.utils;

import gr.cite.earthserver.wcps.parser.evaluation.Query;

public class XWCPSEvalUtils {
	public static final String DEFAULT_XML_RETURN_ELEMENT = "results";

	public static final Query wrapDefaultXmlReturnElement(Query query) {
		query.setCoverageValueMap(null);
		return query.prependValue("<" + XWCPSEvalUtils.DEFAULT_XML_RETURN_ELEMENT + ">")
				.appendValue("</" + XWCPSEvalUtils.DEFAULT_XML_RETURN_ELEMENT + ">");
	}
}
