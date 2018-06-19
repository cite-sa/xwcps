package gr.cite.earthserver.wcps.parser;

import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.mockito.Mockito;

//import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.grammar.XWCPSLexer;
import gr.cite.earthserver.wcps.grammar.XWCPSParser;
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcps.parser.evaluation.XWCPSEvaluationMocks;
import gr.cite.earthserver.wcps.parser.evaluation.visitors.XWCPSEvalVisitor;
import gr.cite.earthserver.wcs.adapter.api.WCSAdapterAPI;
import gr.cite.earthserver.wcs.adapter.WCSAdapter;
import gr.cite.earthserver.wcs.core.Coverage;
import jersey.repackaged.com.google.common.collect.Lists;

public class WCPSEvalVisitorTest {
	
//	@Test
	public void test() {
		String query =
//				"for data in ( frt00003590_07_if164l_trr3 , frt0000b1bd_07_if166l_trr3 ) "
//				+ " where describeCoverage(data)//*[local-name()='cat_solar_longitude' and text()>86.0122]  " // TODO
//				+ " return encode( \n" + 
//				"{ red:(int)(255 / (1 - (1.395 / ((1 - ((data.band_61 - data.band_57)/(data.band_72 - data.band_57))) * 1.370 + ((data.band_61 - data.band_57)/(data.band_72 - data.band_57)) * 1.470)))); \n" + 
//				"green: (int)(255 / (1 - (1.525 / ((1 - ((data.band_80 - data.band_57)/(data.band_124 - data.band_57))) * 1.367 + ((data.band_80 - data.band_57)/(data.band_124 - data.band_57)) * 1.808)))); \n" + 
//				"blue: (int)(255/ (0.5 * (1 - (1.930 / ((1 - ((data.band_142 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_142 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))) * 0.5 * (1 - (1.985 / ((1 - ((data.band_151 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_151 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))))); \n" + 
//				"alpha: (data.band_100 != 65535) * 255 }, \"png\", \"nodata=null\")";
		
		//"for data in ( frt00014174_07_if166s_trr3 ) return <div> describeCoverage(data)//gml:cat_solar_longitude </div>";
				
		//"for c in ( frt00003590_07_if164l_trr3 ) return mixed(encode(1, \"csv\"), describeCoverage(c)//gml:cat_solar_longitude)";
		
		//"for c in ( frt00014174_07_if166s_trr3 ) return describeCoverage(c)//gml:cat_solar_longitude";
				
		//"for c in ( frt00014174_07_if166s_trr3 ) return describeCoverage(c)";
		
		/*"for c in ( frt00014174_07_if166s_trr3 ) "
		+ "where describeCoverage(c)//gml:cat_solar_longitude[text()<86.0122] "
		+ "return describeCoverage(c)//gml:cat_solar_longitude";*/
		
		//"for c in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage "
		//+ "where describeCoverage(c)//gml:cat_solar_longitude[text()<86.0122] "
		//+ "return describeCoverage(c)//gml:cat_solar_longitude";
				
		//"for c in /server/coverage return describeCoverage(c)";
		
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
		//"/server/coverage";
		
//		"for c in ( CCI_V2_monthly_rrs_670 ) return c";
		
		//////////////////////
		//My Working Queries//
		//////////////////////	
				
//		"for data in (frt0000cc22_07_if165l_trr3) return encode( { red: (int)(255 / (max((data.band_233 != 65535) * data.band_233) - min(data.band_233))) * (data.band_233 - min(data.band_233)); green: (int)(255 / (max((data.band_13 != 65535) * data.band_13) - min(data.band_13))) * (data.band_13 - min(data.band_13)); blue: (int)(255 / (max((data.band_78 != 65535) * data.band_78) - min(data.band_78))) * (data.band_78 - min(data.band_78)) ; alpha: (data.band_100 != 65535) * 255}, \"png\", \"nodata=null\")";
		
//		"for data in (frt0000cc22_07_if165l_trr3) return mixed(encode( { red: (int)(255 / (max((data.band_233 != 65535) * data.band_233) - min(data.band_233))) * (data.band_233 - min(data.band_233)); green: (int)(255 / (max((data.band_13 != 65535) * data.band_13) - min(data.band_13))) * (data.band_13 - min(data.band_13)); blue: (int)(255 / (max((data.band_78 != 65535) * data.band_78) - min(data.band_78))) * (data.band_78 - min(data.band_78)) ; alpha: (data.band_100 != 65535) * 255}, \"png\", \"nodata=null\"), metadata(data))";
								
//		"for c in ( frt00009d2f_07_if168s_trr3 ) return encode(c, \"csv\")";
		
//		"for c in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage return encode(c, \"png\")";
				
//		"for c in ( CCI_V2_monthly_rrs_670 ) return metadata(c)";
		
//		"for c in ( CCI_V2_monthly_rrs_670 ) return mixed(encode(1, \"csv\"), metadata(c))";
		
//		"for c in /server/coverage return describeCoverage(c)";
				
//		"for c in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage"
//		+ " return metadata(c)//gml:cat_solar_longitude";
				
//		"for c in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage "
//		+ "where metadata(c)//gml:cat_solar_longitude[text()<86.0122] " 
//		+ "return metadata(c)";
		
//		"for c in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage "
//		+ "where metadata(c)//gml:cat_solar_longitude[text()<86.0122] " 
//		+ "return metadata(c)//gml:cat_solar_longitude";
				
//		"for c in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows' or @endpoint='https://rsg.pml.ac.uk/rasdaman/ows']/coverage"
//		+ " where metadata(c)//gml:cat_solar_longitude[text()<86.0122] "
//		+ " return metadata(c)";


		//New grammar queries
				
//		"for c in ( arsf_test_subset_bands_hmm ) return c::";
//		"for c in ( arsf_test_subset_bands_hmm, CCI_V2_monthly_rrs_670 ) return c::";
//		"for c in ( arsf_test_subset_bands_hmm ) return c:://wcs:CoverageId/text()";
//		"for c in ( * ) return c::";
//		"for c in ( arsf_test_subset_bands_hmm@\"https://rsg.pml.ac.uk/rasdaman/ows\" ) return c::";
//		"for c in ( *@\"https://rsg.pml.ac.uk/rasdaman/ows\" ) return c::";
//		"for c in ( * ) where c:://*[local-name()='RectifiedGrid'][@dimension=2] return c::";
				
		//"for $c in (CCI_V2_monthly_chlor_a@PML, precipitation@ECMWF) return <div> $c:://*[local-name() = 'boundedBy'] </div>";
		//"for $c in (CCI_V2_monthly_chlor_a@PML, precipitation@ECMWF) return $c:://*[local-name() = 'boundedBy']";
			
//		"for $c in CCI_V2_monthly_chlor_a@PML, precipitation@ECMWF orderby $c:://wcs:CoverageId/text() asc return $c:://wcs:CoverageId/text()";

//		"for $c in (CCI_V2_monthly_chlor_a@PML, precipitation@ECMWF) orderby $c:://*[local-name()='RectifiedGrid']/@dimension desc return $c::";

//		"for $c in (CCI_V2_monthly_chlor_a@PML) " +
//		"let test1:=$c::; " +
//		"let test2:=$c:://wcs:CoverageId/text(); " +
//		"let test3:=$c:://*[local-name() = 'boundedBy']; " +
//		"return test1//wcs:CoverageId/text()";

//		"for $c in CCI_V2_monthly_chlor_a " +
//		"let test1:=$c; " +
//		"return encode (test1[ansi(\"2001-07-31T23:59:00\")] * 1000 , \"png\")";

		//"for $c in (CCI_V2_monthly_chlor_a@PML) return $c::";

//		"for $c in (CCI_V2_monthly_chlor_a@PML, precipitation@ECMWF) " +
//		"let test1 := $c:://wcs:CoverageId/text(); " +
//		"orderby test1 desc " +
//		"return test1";

		"for $c in (CCI_V2_monthly_chlor_a@PML) " +
		"let $temp1:=$c:://wcs:CoverageId/text(); " +
		"let $temp2:=$c:://wcs:CoverageId/text(); " +
		"let $tempStatic:=1; " +
		"let $temp4:=$temp1 + $tempStatic; " +

		"let $temp3:=$temp1 + $temp2; " +
		"return $c:://wcs:CoverageId/text()";
		
		System.out.println(query);

		CharStream stream = new ANTLRInputStream(query);
		XWCPSLexer lexer = new XWCPSLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		XWCPSParser parser = new XWCPSParser(tokenStream);

		ParseTree tree = parser.xwcps();

		printInfo(tokenStream, parser, tree);
		
		//WCSAdapterAPI wcsAdapter = new WCSAdapter("http://localhost:8080/femme-application");
		WCSAdapterAPI wcsAdapter = new WCSAdapter("http://earthserver-devel.vhosts.cite.gr/femme-application/");
		//WCSAdapterAPI wcsAdapter = new WCSAdapter("http://es-devel1.local.cite.gr:8080/femme-application-0.0.1-SNAPSHOT");
		//WCSAdapterAPI wcsAdapter = new WCSAdapter("http://es-devel1.local.cite.gr:8080/femme-application-devel");

		XWCPSEvalVisitor visitor = new XWCPSEvalVisitor(wcsAdapter);
		Query result = visitor.visit(tree);

		//System.out.println(result.getQuery());
		System.out.println("Results:");
		System.out.println(result.getValue());
		//try {
			//System.out.println(XMLConverter.nodeToString(XMLConverter.stringToNode(result.getValue(), true), true));
		/*} catch (XMLConversionException e) {
			e.printStackTrace();
		}*/

	}

	private void printInfo(CommonTokenStream tokenStream, XWCPSParser parser, ParseTree tree) {
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
