package gr.cite.earthserver.wcps.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

import gr.cite.earthserver.wcps.grammar.XWCPSLexer;
import gr.cite.earthserver.wcps.grammar.XWCPSParser;

public class XWCPSParseableQueriesTest {

	@Test
	public void query1() {
		parseQuery("for c in ( AvgLandTemp ) return encode(1, \"csv\")");
	}

	@Test
	public void query2() {
		parseQuery("for c in (NIR, AvgLandTemp) return describeCoverage(c)//*[local-name()='domainSet']");
	}

	@Test
	public void query3() {
		parseQuery("for c in (NIR, AvgLandTemp) return describeCoverage(c)");
	}

	@Test
	public void query4() {
		parseQuery(
				"for c in (NIR, AvgLandTemp ) return <a><b atr=describeCoverage(c)//*[local-name()='limits']//text() > describeCoverage(c)//*[local-name()='domainSet']</b></a>");
	}

	@Test
	public void query5() {
		parseQuery(
				"for c in (NIR, AvgLandTemp ) return <a><b>describeCoverage(c)//*[local-name()='domainSet']</b></a>");
	}

	@Test
	public void query6() {
		parseQuery(
				"for c in (NIR, AvgLandTemp ) return <a><b atr=\"yannis\">describeCoverage(c)//*[local-name()='domainSet']</b></a>");
	}

	@Test
	public void query7() {
		parseQuery("/server//coverage/@*[local-name()='test']");
	}

	@Test
	public void query8() {
		parseQuery("/server");
	}

	@Test
	public void query9() {
		parseQuery("for c in /server/coverage return describeCoverage(c)");
	}

	@Test
	public void query10() {
		parseQuery("for c in /server[endpoint='a']/coverage return describeCoverage(c)");
	}

	@Test
	public void query11() {
		parseQuery(
				"for c in ( AvgLandTemp ) return encode(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")], \"csv\")");
	}

	@Test
	public void query12() {
		parseQuery(
				"for c in (NIR, AvgLandTemp ) return <a><b> describeCoverage(c) min( //@*[local-name()='srsDimension'] ) </b></a>");
	}

	@Test
	public void query13() {
		parseQuery(
				"for c in (NIR, AvgLandTemp ) return <a><b> min( describeCoverage(c) //@*[local-name()='srsDimension'] ) </b></a>");
	}

	@Test
	public void query14() {
		parseQuery("for c in (NIR, AvgLandTemp ) return min( describeCoverage(c) //@*[local-name()='srsDimension'] )");
	}

	@Test
	public void query15() {
		parseQuery("for c in (AvgLandTemp) return min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")])");
	}

	@Test
	public void query16() {
		parseQuery(
				"for c in (AvgLandTemp) return <a> min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")]) </a>");
	}

	@Test
	public void query17() {
		parseQuery("for c in (AvgLandTemp) return "
				+ "<a attr=min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")]) > describeCoverage(c) </a>");
	}

	@Test
	public void query18() {
		parseQuery("for c in (AvgLandTemp) return <b> <c attr='test' /> <c></c> <a> 1 </a></b>");
	}

	@Test
	public void query19() {
		parseQuery("for c in (AvgLandTemp) return <b> <c attr='test' /> <c></c> "
				+ "<a> min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")]) </a></b>");
	}

	@Test
	public void query20() {
		parseQuery("for c in (AvgLandTemp) return <b> <c attr='test' /> <c>come text</c> <a> 1 </a></b>");
	}

	public static void parseQuery(String query) {
		CharStream stream = new ANTLRInputStream(query);
		XWCPSLexer lexer = new XWCPSLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		XWCPSParser parser = new XWCPSParser(tokenStream);
		parser.removeErrorListeners();

		parser.xwcps();
		if (parser.getNumberOfSyntaxErrors() > 0) {
			Assert.fail("failed query: " + query);
		}
	}

}
