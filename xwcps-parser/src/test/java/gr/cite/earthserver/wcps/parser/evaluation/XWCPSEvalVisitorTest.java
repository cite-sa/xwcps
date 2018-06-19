package gr.cite.earthserver.wcps.parser.evaluation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

//import gr.cite.earthserver.metadata.core.Coverage;
//import gr.cite.earthserver.wcps.grammar.XWCPSLexer;
//import gr.cite.earthserver.wcps.grammar.XWCPSParser;
import gr.cite.earthserver.wcps.parser.evaluation.visitors.XWCPSEvalVisitor;
import gr.cite.earthserver.wcs.core.WCSRequestBuilder;
//import gr.cite.femme.query.criteria.CriteriaQuery;
import jersey.repackaged.com.google.common.collect.Lists;

public class XWCPSEvalVisitorTest {

//	@Test
//	public void query1() {
//		String query = "for c in (AvgLandTemp) return describeCoverage(c)";
//
//		Query result = executeQuery(query, XWCPSEvaluationMocks.mockDescribeCoverage());
//		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
//				true);
//
//		String formattedExpectedResult = XMLConverter.nodeToString(
//				XMLConverter.stringToNode(XWCPSQueryMockedResponses.SINGLE_DESCRIBE_COVERAGE_RESULT, true), true);
//
//		assertEquals(formattedExpectedResult, formattedActualResult);
//	}
//
//	@Test
//	public void query2() {
//		String query = "for c in (AvgLandTemp , NIR) return describeCoverage(c)";
//
//		Query result = executeQuery(query, XWCPSEvaluationMocks.mockDescribeCoverage());
//		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
//				true);
//
//		String formattedExpectedResult = XMLConverter.nodeToString(
//				XMLConverter.stringToNode(XWCPSQueryMockedResponses.DOUBLE_DESCRIBE_COVERAGE_RESULT, true), true);
//
//		assertEquals(formattedExpectedResult, formattedActualResult);
//	}
//
//	@Test
//	public void query3() {
//		String query = "for c in (AvgLandTemp ) "
//				+ "return <a><b atr=describeCoverage(c)//*[local-name()='limits']//text() > "
//				+ "describeCoverage(c)//*[local-name()='domainSet'] </b></a>";
//
//		Query result = executeQuery(query, XWCPSEvaluationMocks.mockDescribeCoverage());
//		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
//				true);
//
//		String formattedExpectedResult = XMLConverter
//				.nodeToString(XMLConverter.stringToNode(XWCPSQueryMockedResponses.QUERY_3_RESPONSE, true), true);
//
//		assertEquals(formattedExpectedResult, formattedActualResult);
//	}
//
//	@Test
//	public void query4() {
//		String query = "for c in (AvgLandTemp ) "
//				+ "return <a><b atr=\"yannis\">describeCoverage(c)//*[local-name()='domainSet']</b></a>";
//
//		Query result = executeQuery(query, XWCPSEvaluationMocks.mockDescribeCoverage());
//		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
//				true);
//
//		String formattedExpectedResult = XMLConverter
//				.nodeToString(XMLConverter.stringToNode(XWCPSQueryMockedResponses.QUERY_4_RESPONSE, true), true);
//
//		assertEquals(formattedExpectedResult, formattedActualResult);
//	}
//
////	@Test
////	public void query5() {
////		String query = "for c in ( AvgLandTemp ) return encode(1, \"csv\")";
////
////		Query result = executeQuery(query, XWCPSEvaluationMocks.mockProcessCoverages("{1}"));
////
////		assertEquals("{1}", result.getValue());
////	}
//
//	@Test
//	public void query6() {
//		String query = "for c in (NIR, AvgLandTemp ) return <a><b> max( describeCoverage(c) //@*[local-name()='srsDimension'] ) </b></a>";
//
//		Query result = executeQuery(query, XWCPSEvaluationMocks.mockDescribeCoverage());
//
//		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
//				true);
//
//		String formattedExpectedResult = XMLConverter
//				.nodeToString(XMLConverter.stringToNode("<results><a><b>3</b></a></results>", true), true);
//
//		assertEquals(formattedExpectedResult, formattedActualResult);
//	}
//
//	@Test
//	public void query7() {
//		String query = "for c in (NIR, AvgLandTemp ) return max( describeCoverage(c) //@*[local-name()='srsDimension'] )";
//
//		Query result = executeQuery(query, XWCPSEvaluationMocks.mockDescribeCoverage());
//
//		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
//				true);
//
//		String formattedExpectedResult = XMLConverter
//				.nodeToString(XMLConverter.stringToNode("<results>3</results>", true), true);
//
//		assertEquals(formattedExpectedResult, formattedActualResult);
//	}
//
//	@Test
//	public void query8() {
//		String query = "for c in (AvgLandTemp) return min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")])";
//
//		Query result = executeQuery(query, XWCPSEvaluationMocks.mockProcessCoverages("2.2834647"));
//
//		assertEquals("2.2834647", result.getValue());
//	}
//
//	@Test
//	public void query9() {
//		String query = "for c in (AvgLandTemp) return <a>min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")])</a>";
//
//		Query result = executeQuery(query,
//				XWCPSEvaluationMocks.mockProcessCoverages(
//						"for c in ( AvgLandTemp ) return min ( c [ Lat ( 53.08 ) , Long ( 8.80 ) , ansi ( \"2014-01\" : \"2014-12\" ) ] )",
//						"2.2834647"));
//
//		assertEquals("<results><a>2.2834647</a></results>", result.getValue());
//	}
//
//	@Test
//	public void query10() {
//		String query = "for c in (AvgLandTemp) return <a attr=min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")]) > describeCoverage(c) </a>";
//
//		Query result = executeQuery(query,
//				XWCPSEvaluationMocks.mockDescribeCoverage(XWCPSEvaluationMocks.mockProcessCoverages(
//						"for c in ( AvgLandTemp ) return min ( c [ Lat ( 53.08 ) , Long ( 8.80 ) , ansi ( \"2014-01\" : \"2014-12\" ) ] )",
//						"2.2834647")));
//
//		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
//				true);
//
//		String formattedExpectedResult = XMLConverter
//				.nodeToString(XMLConverter.stringToNode(XWCPSQueryMockedResponses.QUERY_10_RESPONSE, true), true);
//
//		assertEquals(formattedExpectedResult, formattedActualResult);
//
//	}
//
//	@Test
//	public void query11() {
//		String query = "for c in //coverage return describeCoverage(c)";
//
//		Query result = executeQuery(query, XWCPSEvaluationMocks.mockDescribeCoverage(),
//				XWCPSEvaluationMocks.mockCriteriaQuery(dummyCoverages()));
//		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
//				true);
//
//		String formattedExpectedResult = XMLConverter.nodeToString(
//				XMLConverter.stringToNode(XWCPSQueryMockedResponses.DOUBLE_DESCRIBE_COVERAGE_RESULT, true), true);
//
//		assertEquals(formattedExpectedResult, formattedActualResult);
//	}
//
//	@Test
//	public void query12() {
//		String query = "for c in /server[@endpoint='abc']/coverage return describeCoverage(c)";
//
//		Query result = executeQuery(query, XWCPSEvaluationMocks.mockDescribeCoverage(),
//				XWCPSEvaluationMocks.mockCriteriaQuery(dummyCoverages()));
//		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
//				true);
//
//		String formattedExpectedResult = XMLConverter.nodeToString(
//				XMLConverter.stringToNode(XWCPSQueryMockedResponses.DOUBLE_DESCRIBE_COVERAGE_RESULT, true), true);
//
//		assertEquals(formattedExpectedResult, formattedActualResult);
//	}
//
//	private static ArrayList<Coverage> dummyCoverages() {
//		return Lists.newArrayList(new Coverage() {
//			{
//				setLocalId("AvgLandTemp");
//			}
//		}, new Coverage() {
//			{
//				setLocalId("NIR");
//			}
//		});
//	}
//
//	@Test
//	public void query13() {
//		String query = "for c in //coverage return min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")])";
//
//		Query result = executeQuery(query,
//				XWCPSEvaluationMocks.mockDescribeCoverage(XWCPSEvaluationMocks.mockProcessCoverages(
//						"for c in ( AvgLandTemp ) return min ( c [ Lat ( 53.08 ) , Long ( 8.80 ) , ansi ( \"2014-01\" : \"2014-12\" ) ] )",
//						"2.2834647")),
//				XWCPSEvaluationMocks.mockCriteriaQuery(Lists.newArrayList(new Coverage() {
//					{
//						setLocalId("AvgLandTemp");
//					}
//				})));
//
//		assertEquals("2.2834647", result.getValue());
//	}
//
//	@Test
//	public void query14() {
//		String query = "for c in //coverage[id='AvgLandTemp'] return min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")])";
//
//		Query result = executeQuery(query,
//				XWCPSEvaluationMocks.mockProcessCoverages(
//						"for c in ( AvgLandTemp ) return min ( c [ Lat ( 53.08 ) , Long ( 8.80 ) , ansi ( \"2014-01\" : \"2014-12\" ) ] )",
//						"2.2834647"),
//				XWCPSEvaluationMocks.mockCriteriaQuery(Lists.newArrayList(new Coverage() {
//					{
//						setLocalId("AvgLandTemp");
//					}
//				})));
//
//		assertEquals("2.2834647", result.getValue());
//	}
//
//	@Test
//	public void query15() {
//		String query = "for c in //coverage return wrap-result(describeCoverage(c), < p attr=min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")]) > <a>'text'</a> )";
//
//		Query result = executeQuery(query,
//				XWCPSEvaluationMocks.mockDescribeCoverage(XWCPSEvaluationMocks.mockProcessCoverages(
//						"for c in ( AvgLandTemp ) return min ( c [ Lat ( 53.08 ) , Long ( 8.80 ) , ansi ( \"2014-01\" : \"2014-12\" ) ] )",
//						"2.2834647")),
//				XWCPSEvaluationMocks.mockCriteriaQuery(Lists.newArrayList(new Coverage() {
//					{
//						setLocalId("AvgLandTemp");
//					}
//				})));
//
//		// System.out.println(result.getValue());
//
//		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
//				true);
//
//		String formattedExpectedResult = XMLConverter
//				.nodeToString(XMLConverter.stringToNode(XWCPSQueryMockedResponses.QUERY_15_RESPONSE, true), true);
//
//		assertEquals(formattedExpectedResult, formattedActualResult);
//	}
//
//	static Query executeQuery(String query, WCSRequestBuilder builder, CriteriaQuery<Coverage> exmmsQuery) {
//		CharStream stream = new ANTLRInputStream(query);
//		XWCPSLexer lexer = new XWCPSLexer(stream);
//		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
//		XWCPSParser parser = new XWCPSParser(tokenStream);
//
//		ParseTree tree = parser.xwcps();
//
//		XWCPSEvalVisitor visitor = new XWCPSEvalVisitor(builder, exmmsQuery);
//		Query result = visitor.visit(tree);
//		return result;
//	}
//
//	static Query executeQuery(String query, WCSRequestBuilder builder) {
//		return executeQuery(query, builder, null);
//	}

}
