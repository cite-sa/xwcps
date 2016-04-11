package gr.cite.earthserver.wcps.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
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
		parseQuery(
				"for c in (AvgLandTemp) return <b> <c attr='test' /> \"some text1\" <c>'some text2'</c> <a> min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")]) </a></b>");
	}

	@Test
	public void letQuery1() {
		parseQuery("let d:= 10 for c in /server/coverage return describeCoverage(c)");
	}

	@Test
	public void letQuery2() {
		parseQuery("let d:= 10 for c in /server/coverage return <r> <b>d</b> <a> describeCoverage(c)</a> </r>");
	}

	@Test
	public void letQuery3() {
		parseQuery("let v1:= 1 " + "let v2:= 2 " + "for c in /server/coverage return <r> <b> v1 </b> <a> v2 </a> </r>");
	}

	@Test
	public void letQuery4() {
		parseQuery("let v1:= 1 " + "for c in /server/coverage " + "let v3:= 3 " + "return <r> " + "<b> v1 </b> "
				+ "<a> v3 </a> " + "</r>");
	}

	@Test
	public void letQuery5() {
		parseQuery("let v1:= 1 "

				+ "for c in /server/coverage "

				+ "let v3:= 3 + v1 "

				+ "return <r> " + "<b> v1 </b> " + "<a> v3 </a> " + "</r>");
	}

	@Test
	public void letQuery6() {
		parseQuery("let v1 := 1 "

				+ "for c in /server/coverage "

				+ "let v3 := 3 + v1 "

				+ "let v4 := <e> v3 </e> "

				+ "return <r> v4 + 1 </r>");
	}

	@Test
	public void letQuery7() {
		parseQuery("let v1 := 1 "

				+ "for c in /server/coverage "

				+ "let v3 := v1 + describeCoverage(c)/somePath/@someValue "

				+ "let v4 := <e> v3 </e> "

				+ "return <r> v4 + 5 </r>");
	}

	@Test
	public void letQuery8() {
		parseQuery("let v1 := 1 "

				+ "for c in /server/coverage "

				+ "let v3 := v1 + describeCoverage(c)/somePath/@someValue "

				+ "let v4 := <e> v3 </e> "

				+ "return <r> v4 + describeCoverage(c)/somePath/@someValue </r>");
	}

	@Test
	public void wcpsQuery1() {
		parseQuery("for data in ( frt00003590_07_if164l_trr3 ) return encode(  \n"
				+ "{red: (int)(255 / (1 - (1.395 / ((1 - ((61 - 57)/(72 - 57))) * 1.370 + ((61 - 57)/(72 - data.band_57)) * 1.470))));  \n"
				+ "green: 1;  \n" + "blue:  1;  \n" + "alpha: 255 }, \"png\", \"nodata=null\")");
	}

	@Test
	public void wcpsQuery2() {
		parseQuery("for data in ( frt00003590_07_if164l_trr3 ) return encode( \n"
				+ "{ red:(int)(255 / (1 - (1.395 / ((1 - ((data.band_61 - data.band_57)/(data.band_72 - data.band_57))) * 1.370 + ((data.band_61 - data.band_57)/(data.band_72 - data.band_57)) * 1.470))));  \n"
				+ "green: (int)(255 / (1 - (1.525 / ((1 - ((data.band_80 - data.band_57)/(data.band_124 - data.band_57))) * 1.367 + ((data.band_80 - data.band_57)/(data.band_124 - data.band_57)) * 1.808))));  \n"
				+ "blue:  (int)(255/ (0.5 * (1 - (1.930 / ((1 - ((data.band_142 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_142 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))) * 0.5 * (1 - (1.985 / ((1 - ((data.band_151 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_151 - data.band_130)/(data.band_163 - data.band_130)) * 2.067)))));  \n"
				+ "alpha: (data.band_100 != 65535) * 255 }, \"png\", \"nodata=null\")");
	}

	public static void parseQuery(String query) {
		CharStream stream = new ANTLRInputStream(query);
		XWCPSLexer lexer = new XWCPSLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		XWCPSParser parser = new XWCPSParser(tokenStream);
		parser.removeErrorListeners();

		final List<String> errors = new ArrayList<>();

		parser.addErrorListener(new BaseErrorListener() {
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
					int charPositionInLine, String msg, RecognitionException e) {
				errors.add(line + ":" + charPositionInLine + " :: " + msg);
			}
		});

		parser.xwcps();
		if (parser.getNumberOfSyntaxErrors() > 0) {
			Assert.fail("failed query: '" + query + "' -- " + errors.stream().collect(Collectors.joining(", ")));
		}
	}

}
