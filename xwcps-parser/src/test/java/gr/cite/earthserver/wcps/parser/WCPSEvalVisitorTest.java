package gr.cite.earthserver.wcps.parser;

import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.grammar.XWCPSLexer;
import gr.cite.earthserver.wcps.grammar.XWCPSParser;
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcps.parser.evaluation.XWCPSEvalVisitor;
import gr.cite.earthserver.wcps.parser.evaluation.XWCPSEvaluationMocks;
import gr.cite.scarabaeus.utils.xml.XMLConverter;
import jersey.repackaged.com.google.common.collect.Lists;

public class WCPSEvalVisitorTest {
	private static final String WCS_ENDPOINT = "http://flanche.com:9090/rasdaman/ows";

	@Test
	public void test() {
		String query =
		// TODO
		// "for c in (AvgLandTemp) return <a attr=min(c[Lat(53.08), Long(8.80),
		// ansi(\"2014-01\":\"2014-12\")]) > describeCoverage(c) </a>";
		// "for c in /server[@endpoint='example.com' and @id =
		// '1']/coverage[@id='NIR' and @guid='myGUID' and @foo='1']//dataset
		// return describeCoverage(c)";
		// "for c in /server[@endpoint='example.com']/coverage[@id='NIR' or
		// (@foo='1' and @bar='1' or (@koo='2' and @boo='2'))]//dataSet[@id='5'
		// or @loo='abc'] return describeCoverage(c)";

		"for c in //coverage[id='AvgLandTemp'] return min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")])";

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
				XWCPSEvaluationMocks.mockCriteriaQuery(Lists.newArrayList(new Coverage() {
					{
						setLocalId("NIR");
					}
				}, new Coverage() {
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
