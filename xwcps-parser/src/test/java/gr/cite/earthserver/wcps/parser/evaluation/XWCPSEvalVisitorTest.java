package gr.cite.earthserver.wcps.parser.evaluation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import gr.cite.earthserver.wcps.grammar.XWCPSLexer;
import gr.cite.earthserver.wcps.grammar.XWCPSParser;
import gr.cite.earthserver.wcs.client.WCSRequest;
import gr.cite.earthserver.wcs.client.WCSRequestBuilder;
import gr.cite.earthserver.wcs.client.WCSRequestBuilder.DescribeCoverage;
import gr.cite.earthserver.wcs.client.WCSRequestBuilder.ProcessCoverages;
import gr.cite.scarabaeus.utils.xml.XMLConverter;
import gr.cite.earthserver.wcs.client.WCSRequestException;

public class XWCPSEvalVisitorTest {

	@Test
	public void query1() {
		String query = "for c in (AvgLandTemp) return describeCoverage(c)";

		Query result = executeQuery(query, mockDescribeCoverage());
		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
				true);

		String formattedExpectedResult = XMLConverter.nodeToString(
				XMLConverter.stringToNode(XWCPSQueryMockedResponses.SINGLE_DESCRIBE_COVERAGE_RESULT, true), true);

		assertEquals(formattedExpectedResult, formattedActualResult);
	}

	@Test
	public void query2() {
		String query = "for c in (AvgLandTemp , NIR) return describeCoverage(c)";

		Query result = executeQuery(query, mockDescribeCoverage());
		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
				true);

		String formattedExpectedResult = XMLConverter.nodeToString(
				XMLConverter.stringToNode(XWCPSQueryMockedResponses.DOUBLE_DESCRIBE_COVERAGE_RESULT, true), true);

		assertEquals(formattedExpectedResult, formattedActualResult);
	}

	@Test
	public void query3() {
		String query = "for c in (AvgLandTemp ) "
				+ "return <a><b atr=describeCoverage(c)//*[local-name()='limits']//text() > "
				+ "describeCoverage(c)//*[local-name()='domainSet'] </b></a>";

		Query result = executeQuery(query, mockDescribeCoverage());
		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
				true);

		String formattedExpectedResult = XMLConverter
				.nodeToString(XMLConverter.stringToNode(XWCPSQueryMockedResponses.QUERY_3_RESPONSE, true), true);

		assertEquals(formattedExpectedResult, formattedActualResult);
	}

	@Test
	public void query4() {
		String query = "for c in (AvgLandTemp ) "
				+ "return <a><b atr=\"yannis\">describeCoverage(c)//*[local-name()='domainSet']</b></a>";

		Query result = executeQuery(query, mockDescribeCoverage());
		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
				true);

		String formattedExpectedResult = XMLConverter
				.nodeToString(XMLConverter.stringToNode(XWCPSQueryMockedResponses.QUERY_4_RESPONSE, true), true);

		assertEquals(formattedExpectedResult, formattedActualResult);
	}

	@Test
	public void query5() {
		String query = "for c in ( AvgLandTemp ) return encode(1, \"csv\")";

		Query result = executeQuery(query, mockProcessCoverages("{1}"));

		assertEquals("{1}", result.getValue());
	}

	@Test
	public void query6() {
		String query = "for c in (NIR, AvgLandTemp ) return <a><b> max( describeCoverage(c) //@*[local-name()='srsDimension'] ) </b></a>";

		Query result = executeQuery(query, mockDescribeCoverage());

		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
				true);

		String formattedExpectedResult = XMLConverter.nodeToString(XMLConverter.stringToNode("<a><b>3</b></a>", true),
				true);

		assertEquals(formattedExpectedResult, formattedActualResult);
	}

	@Test
	public void query7() {
		String query = "for c in (NIR, AvgLandTemp ) return max( describeCoverage(c) //@*[local-name()='srsDimension'] )";

		Query result = executeQuery(query, mockDescribeCoverage());

		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
				true);

		String formattedExpectedResult = XMLConverter
				.nodeToString(XMLConverter.stringToNode("<results>3</results>", true), true);

		assertEquals(formattedExpectedResult, formattedActualResult);
	}

	@Test
	public void query8() {
		String query = "for c in (AvgLandTemp) return min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")])";

		Query result = executeQuery(query, mockProcessCoverages("2.2834647"));

		assertEquals("2.2834647", result.getValue());
	}

	@Test
	public void query9() {
		String query = "for c in (AvgLandTemp) return <a>min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")])</a>";

		Query result = executeQuery(query,
				mockProcessCoverages(
						"for c in ( AvgLandTemp ) return min ( c [ Lat ( 53.08 ) , Long ( 8.80 ) , ansi ( \"2014-01\" : \"2014-12\" ) ] )",
						"2.2834647"));

		assertEquals("<a>2.2834647</a>", result.getValue());
	}

	@Test
	public void query10() {
		String query = "for c in (AvgLandTemp) return <a attr=min(c[Lat(53.08), Long(8.80), ansi(\"2014-01\":\"2014-12\")]) > describeCoverage(c) </a>";

		Query result = executeQuery(query,
				mockDescribeCoverage(mockProcessCoverages(
						"for c in ( AvgLandTemp ) return min ( c [ Lat ( 53.08 ) , Long ( 8.80 ) , ansi ( \"2014-01\" : \"2014-12\" ) ] )",
						"2.2834647")));

		String formattedActualResult = XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true),
				true);

		String formattedExpectedResult = XMLConverter
				.nodeToString(XMLConverter.stringToNode(XWCPSQueryMockedResponses.QUERY_10_RESPONSE, true), true);

		assertEquals(formattedExpectedResult, formattedActualResult);

	}

	static Query executeQuery(String query, WCSRequestBuilder builder) {
		CharStream stream = new ANTLRInputStream(query);
		XWCPSLexer lexer = new XWCPSLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		XWCPSParser parser = new XWCPSParser(tokenStream);

		ParseTree tree = parser.xwcps();

		XWCPSEvalVisitor visitor = new XWCPSEvalVisitor(builder);
		Query result = visitor.visit(tree);
		return result;
	}

	private static WCSRequestBuilder mockWCSRequestBuilder() {
		WCSRequestBuilder requestBuilder = mock(WCSRequestBuilder.class);

		// TODO mock GetCapabilities
		// TODO mock GetCoverage
		return requestBuilder;
	}

	private static WCSRequestBuilder mockDescribeCoverage() {
		WCSRequestBuilder requestBuilder = mockWCSRequestBuilder();
		return mockDescribeCoverage(requestBuilder);
	}

	private static WCSRequestBuilder mockDescribeCoverage(WCSRequestBuilder requestBuilder) {
		DescribeCoverage describeCoverage = mock(DescribeCoverage.class);

		when(requestBuilder.describeCoverage()).thenReturn(describeCoverage);

		WCSRequest wcsRequest = mock(WCSRequest.class);
		when(describeCoverage.coverageId(anyString())).thenReturn(describeCoverage);
		when(describeCoverage.build()).thenReturn(wcsRequest);

		try {
			when(wcsRequest.get()).thenReturn(XWCPSQueryMockedResponses.AVGLANDTEMP_DESCRIBE_COVERAGE);
		} catch (WCSRequestException e) {
		}

		return requestBuilder;
	}

	private static WCSRequestBuilder mockProcessCoverages(String wcpsQueryResponse) {
		WCSRequestBuilder requestBuilder = mockWCSRequestBuilder();
		ProcessCoverages processCoverages = mock(ProcessCoverages.class);
		when(requestBuilder.processCoverages()).thenReturn(processCoverages);

		WCSRequest wcsRequest = mock(WCSRequest.class);
		when(processCoverages.query(anyString())).thenReturn(processCoverages);
		when(processCoverages.build()).thenReturn(wcsRequest);

		try {
			when(wcsRequest.get()).thenReturn(wcpsQueryResponse);
		} catch (WCSRequestException e) {
		}

		return requestBuilder;
	}

	private static WCSRequestBuilder mockProcessCoverages(String query, String wcpsQueryResponse) {
		WCSRequestBuilder requestBuilder = mockWCSRequestBuilder();
		ProcessCoverages processCoverages = mock(ProcessCoverages.class);
		when(requestBuilder.processCoverages()).thenReturn(processCoverages);

		WCSRequest wcsRequest = mock(WCSRequest.class);
		when(processCoverages.query(query)).thenReturn(processCoverages);
		when(processCoverages.build()).thenReturn(wcsRequest);

		try {
			when(wcsRequest.get()).thenReturn(wcpsQueryResponse);
		} catch (WCSRequestException e) {
		}

		return requestBuilder;
	}

}
