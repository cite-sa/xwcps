package gr.cite.earthserver.wcps.parser;

import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.mockito.Mockito;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.grammar.XWCPSLexer;
import gr.cite.earthserver.wcps.grammar.XWCPSParser;
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcps.parser.evaluation.XWCPSEvalVisitor;
import gr.cite.earthserver.wcps.parser.evaluation.XWCPSEvaluationMocks;
import gr.cite.scarabaeus.utils.xml.XMLConverter;
import jersey.repackaged.com.google.common.collect.Lists;

public class WCPSEvalVisitorTest {
//	private static final String WCS_ENDPOINT = "http://flanche.com:9090/rasdaman/ows";

	private static final String WCS_ENDPOINT = "http://access.planetserver.eu:8080/rasdaman/ows";

	
	@Test
	public void test() {
		String query =
				"for data in ( frt00003590_07_if164l_trr3 ) return encode( \n" + 
				"{ red:(int)(255 / (1 - (1.395 / ((1 - ((data.band_61 - data.band_57)/(data.band_72 - data.band_57))) * 1.370 + ((data.band_61 - data.band_57)/(data.band_72 - data.band_57)) * 1.470)))); \n" + 
				"green: (int)(255 / (1 - (1.525 / ((1 - ((data.band_80 - data.band_57)/(data.band_124 - data.band_57))) * 1.367 + ((data.band_80 - data.band_57)/(data.band_124 - data.band_57)) * 1.808)))); \n" + 
				"blue: (int)(255/ (0.5 * (1 - (1.930 / ((1 - ((data.band_142 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_142 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))) * 0.5 * (1 - (1.985 / ((1 - ((data.band_151 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_151 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))))); \n" + 
				"alpha: (data.band_100 != 65535) * 255 }, \"png\", \"nodata=null\")";
		// TODO
		// "for c in (AvgLandTemp) return <a attr=min(c[Lat(53.08), Long(8.80),
		// ansi(\"2014-01\":\"2014-12\")]) > describeCoverage(c) </a>";
//				"for c in (AvgLandTemp , NIR) return describeCoverage(c)";
//				"for c in (AvgLandTemp) "
//				+ "return <a>'text1'<b "
////				+ ">"
//				+ "attr='yannis' >"
////				+ "atr=describeCoverage(c)//*[local-name()='limits']//text() > "
//				+ "describeCoverage(c)//*[local-name()='domainSet'] </b>"
//				+ "'text2'</a>";

//		"for c in  return min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")])";
//		"for c in (AvgLandTemp) return <b> <c attr='test' /> \"some text\" <c>'some text'</c> <a> min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")]) </a></b>";

		// "/server//coverage/@*[local-name()='test']";
		// "/server";

		CharStream stream = new ANTLRInputStream(query);
		XWCPSLexer lexer = new XWCPSLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		XWCPSParser parser = new XWCPSParser(tokenStream);

		ParseTree tree = parser.xwcps();

		System.out.println(tokenStream.getTokens().stream().map(token -> {
			String channelStr = "";
			String txt = token.getText();
			if (txt != null) {
				txt = txt.replace("\n", "\\n");
				txt = txt.replace("\r", "\\r");
				txt = txt.replace("\t", "\\t");
			} else {
				txt = "<no text>";
			}
			return "[@" + token.getTokenIndex() + "," + token.getStartIndex() + ":" + token.getStopIndex() + "='" + txt
					+ "',<" + (token.getType() >= 0 ? XWCPSLexer.ruleNames[token.getType() - 1] : "-1") + ">"
					+ channelStr + "," + token.getLine() + ":" + token.getCharPositionInLine() + "]";
		}).collect(Collectors.toList()).toString());

		System.out.println(tokenStream.getTokens());
		System.out.println(tree.toStringTree(parser));

		XWCPSEvalVisitor visitor = new XWCPSEvalVisitor(WCS_ENDPOINT,
				XWCPSEvaluationMocks.mockCriteriaQuery(Lists.newArrayList(/*new Coverage() {
					{
						setLocalId("NIR");
					}
				},*/ new Coverage() {
					{
						setLocalId("AvgLandTemp");
					}
				}))
				);
		Query result = visitor.visit(tree);

		System.out.println(result.getQuery());
		System.out.println(result.getValue());
		System.out.println(XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true), true));

	}

	void xpath2() throws Exception {

		String sxpath = "min(//b/@v)"; // xpath 2.0 function; this provides a
										// clean demo of xpath 2.0 support

		String xml = "<a>\n" + "<b v=\"2\" />\n" + "<b v=\"1\" />\n" + "<b v=\"3\" />\n" + "</a>";

		// System.out.println(new
		// XPathEvaluator(XMLConverter.stringToNode(xml)).evaluate(sxpath));
	}

	public static void main(String[] args) throws Exception {
		new WCPSEvalVisitorTest().test();
	}
}
