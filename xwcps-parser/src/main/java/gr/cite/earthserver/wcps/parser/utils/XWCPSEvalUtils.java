package gr.cite.earthserver.wcps.parser.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.OpenXmlElementContext;
import gr.cite.earthserver.wcps.parser.core.XwcpsReturnValue;
import gr.cite.earthserver.wcps.parser.evaluation.Query;

public class XWCPSEvalUtils {
	public static final String DEFAULT_XML_RETURN_ELEMENT = "results";

	public static final Query wrapDefaultXmlReturnElement(Query query) {
		query.setCoverageValueMap(null);
		return query.prependValue("<" + XWCPSEvalUtils.DEFAULT_XML_RETURN_ELEMENT + ">")
				.appendValue("</" + XWCPSEvalUtils.DEFAULT_XML_RETURN_ELEMENT + ">");
	}

	public static String getElementName(OpenXmlElementContext ctx) {
		return ctx.xmlElement().qName().getText();
	}

	public static String removeQuates(String str) {
		return str.replaceAll("'|\"", "");
	}

	public static List<Query> constructForQueries(String variable, List<Coverage> coverages) {
		return coverages.stream().map(c -> {

			Map<Coverage, XwcpsReturnValue> coverageMap = new HashMap<>();
			coverageMap.put(c, null);

			Query q = new Query().setQuery("for " + variable + " in ( " + c.getLocalId() + " ) ")
					.setCoverageValueMap(coverageMap);

			return q;
		}).collect(Collectors.toList());
	}
}
