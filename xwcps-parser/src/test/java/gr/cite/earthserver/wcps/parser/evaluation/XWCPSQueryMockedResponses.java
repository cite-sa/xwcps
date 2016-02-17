package gr.cite.earthserver.wcps.parser.evaluation;

public final class XWCPSQueryMockedResponses {

	public static final String AVGLANDTEMP_DESCRIBE_COVERAGE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<wcs:CoverageDescriptions xsi:schemaLocation=\"http://www.opengis.net/wcs/2.0 http://schemas.opengis.net/wcs/2.0/wcsAll.xsd\" xmlns:wcs=\"http://www.opengis.net/wcs/2.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:crs=\"http://www.opengis.net/wcs/service-extension/crs/1.0\" xmlns:ows=\"http://www.opengis.net/ows/2.0\" xmlns:gml=\"http://www.opengis.net/gml/3.2\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n"
			+ "  <wcs:CoverageDescription gml:id=\"AvgLandTemp\" xmlns=\"http://www.opengis.net/gml/3.2\" xmlns:gmlcov=\"http://www.opengis.net/gmlcov/1.0\" xmlns:swe=\"http://www.opengis.net/swe/2.0\">\n"
			+ "    <boundedBy>\n"
			+ "      <Envelope srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\" axisLabels=\"Lat Long ansi\" uomLabels=\"degree degree d\" srsDimension=\"3\">\n"
			+ "        <lowerCorner>-90 -180 145763</lowerCorner>\n"
			+ "        <upperCorner>90 180 151362</upperCorner>\n" + "      </Envelope>\n" + "    </boundedBy>\n"
			+ "    <wcs:CoverageId>AvgLandTemp</wcs:CoverageId>\n" + "    <gmlcov:metadata/>\n" + "    <domainSet>\n"
			+ "      <gmlrgrid:ReferenceableGridByVectors dimension=\"3\" gml:id=\"AvgLandTemp-grid\" xsi:schemaLocation=\"http://www.opengis.net/gml/3.3/rgrid http://schemas.opengis.net/gml/3.3/referenceableGrid.xsd\" xmlns:gmlrgrid=\"http://www.opengis.net/gml/3.3/rgrid\">\n"
			+ "        <limits>\n" + "          <GridEnvelope>\n" + "            <low>0 0 0</low>\n"
			+ "            <high>3599 1799 184</high>\n" + "          </GridEnvelope>\n" + "        </limits>\n"
			+ "        <axisLabels>Long Lat ansi</axisLabels>\n" + "        <gmlrgrid:origin>\n"
			+ "          <Point gml:id=\"AvgLandTemp-origin\" srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\" axisLabels=\"Lat Long ansi\" uomLabels=\"degree degree d\" srsDimension=\"3\">\n"
			+ "            <pos>89.95 -179.95 145763</pos>\n" + "          </Point>\n" + "        </gmlrgrid:origin>\n"
			+ "        <gmlrgrid:generalGridAxis>\n" + "          <gmlrgrid:GeneralGridAxis>\n"
			+ "            <gmlrgrid:offsetVector srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\" axisLabels=\"Lat Long ansi\" uomLabels=\"degree degree d\" srsDimension=\"3\">0 0.1 0</gmlrgrid:offsetVector>\n"
			+ "            <gmlrgrid:coefficients/>\n"
			+ "            <gmlrgrid:gridAxesSpanned>Long</gmlrgrid:gridAxesSpanned>\n"
			+ "            <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "          </gmlrgrid:GeneralGridAxis>\n" + "        </gmlrgrid:generalGridAxis>\n"
			+ "        <gmlrgrid:generalGridAxis>\n" + "          <gmlrgrid:GeneralGridAxis>\n"
			+ "            <gmlrgrid:offsetVector srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\" axisLabels=\"Lat Long ansi\" uomLabels=\"degree degree d\" srsDimension=\"3\">-0.1 0 0</gmlrgrid:offsetVector>\n"
			+ "            <gmlrgrid:coefficients/>\n"
			+ "            <gmlrgrid:gridAxesSpanned>Lat</gmlrgrid:gridAxesSpanned>\n"
			+ "            <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "          </gmlrgrid:GeneralGridAxis>\n" + "        </gmlrgrid:generalGridAxis>\n"
			+ "        <gmlrgrid:generalGridAxis>\n" + "          <gmlrgrid:GeneralGridAxis>\n"
			+ "            <gmlrgrid:offsetVector srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\" axisLabels=\"Lat Long ansi\" uomLabels=\"degree degree d\" srsDimension=\"3\">0 0 1</gmlrgrid:offsetVector>\n"
			+ "            <gmlrgrid:coefficients>0 29 60 90 121 151 182 213 243 274 304 335 366 394 425 455 486 516 547 578 608 639 669 700 731 759 790 820 851 881 912 943 973 1004 1034 1065 1096 1124 1155 1185 1216 1246 1277 1308 1338 1369 1399 1430 1461 1490 1521 1551 1582 1612 1643 1674 1704 1735 1765 1796 1827 1855 1886 1916 1947 1977 2008 2039 2069 2100 2130 2161 2192 2220 2251 2281 2312 2342 2373 2404 2434 2465 2495 2526 2557 2585 2616 2646 2677 2707 2738 2769 2799 2830 2860 2891 2922 2951 2982 3012 3043 3073 3104 3135 3165 3196 3226 3257 3288 3316 3347 3377 3408 3438 3469 3500 3530 3561 3591 3622 3653 3681 3712 3742 3773 3803 3834 3865 3895 3926 3956 3987 4018 4046 4077 4107 4138 4168 4199 4230 4260 4291 4321 4352 4383 4412 4443 4473 4504 4534 4565 4596 4626 4657 4687 4718 4749 4777 4808 4838 4869 4899 4930 4961 4991 5022 5052 5083 5114 5142 5173 5203 5234 5264 5295 5326 5356 5387 5417 5448 5479 5507 5538 5568 5599</gmlrgrid:coefficients>\n"
			+ "            <gmlrgrid:gridAxesSpanned>ansi</gmlrgrid:gridAxesSpanned>\n"
			+ "            <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "          </gmlrgrid:GeneralGridAxis>\n" + "        </gmlrgrid:generalGridAxis>\n"
			+ "      </gmlrgrid:ReferenceableGridByVectors>\n" + "    </domainSet>\n" + "    <gmlcov:rangeType>\n"
			+ "      <swe:DataRecord>\n" + "        <swe:field name=\"Gray\">\n" + "          <swe:Quantity>\n"
			+ "            <swe:nilValues>\n" + "              <swe:NilValues/>\n" + "            </swe:nilValues>\n"
			+ "            <swe:uom code=\"10^0\"/>\n" + "          </swe:Quantity>\n" + "        </swe:field>\n"
			+ "      </swe:DataRecord>\n" + "    </gmlcov:rangeType>\n" + "    <wcs:ServiceParameters>\n"
			+ "      <wcs:CoverageSubtype>ReferenceableGridCoverage</wcs:CoverageSubtype>\n"
			+ "      <CoverageSubtypeParent xmlns=\"http://www.opengis.net/wcs/2.0\">\n"
			+ "        <CoverageSubtype>AbstractDiscreteCoverage</CoverageSubtype>\n"
			+ "        <CoverageSubtypeParent>\n" + "          <CoverageSubtype>AbstractCoverage</CoverageSubtype>\n"
			+ "        </CoverageSubtypeParent>\n" + "      </CoverageSubtypeParent>\n"
			+ "      <wcs:nativeFormat>application/octet-stream</wcs:nativeFormat>\n" + "    </wcs:ServiceParameters>\n"
			+ "  </wcs:CoverageDescription>\n" + "</wcs:CoverageDescriptions>\n";

	public static final String SINGLE_DESCRIBE_COVERAGE_RESULT = "<results>\n" + "   <coverages>\n"
			+ "      <coverage id=\"AvgLandTemp\">\n"
			+ "         <wcs:CoverageDescriptions xmlns:wcs=\"http://www.opengis.net/wcs/2.0\"\n"
			+ "                                   xmlns:crs=\"http://www.opengis.net/wcs/service-extension/crs/1.0\"\n"
			+ "                                   xmlns:gml=\"http://www.opengis.net/gml/3.2\"\n"
			+ "                                   xmlns:ows=\"http://www.opengis.net/ows/2.0\"\n"
			+ "                                   xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n"
			+ "                                   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
			+ "                                   xsi:schemaLocation=\"http://www.opengis.net/wcs/2.0 http://schemas.opengis.net/wcs/2.0/wcsAll.xsd\">\n"
			+ "            <wcs:CoverageDescription xmlns=\"http://www.opengis.net/gml/3.2\"\n"
			+ "                                     xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n"
			+ "                                     xmlns:gmlcov=\"http://www.opengis.net/gmlcov/1.0\"\n"
			+ "                                     gml:id=\"AvgLandTemp\">\n" + "               <boundedBy>\n"
			+ "                  <Envelope axisLabels=\"Lat Long ansi\"\n"
			+ "                            srsDimension=\"3\"\n"
			+ "                            srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                            uomLabels=\"degree degree d\">\n"
			+ "                     <lowerCorner>-90 -180 145763</lowerCorner>\n"
			+ "                     <upperCorner>90 180 151362</upperCorner>\n" + "                  </Envelope>\n"
			+ "               </boundedBy>\n" + "               <wcs:CoverageId>AvgLandTemp</wcs:CoverageId>\n"
			+ "               <gmlcov:metadata/>\n" + "               <domainSet>\n"
			+ "                  <gmlrgrid:ReferenceableGridByVectors xmlns:gmlrgrid=\"http://www.opengis.net/gml/3.3/rgrid\"\n"
			+ "                                                       dimension=\"3\"\n"
			+ "                                                       gml:id=\"AvgLandTemp-grid\"\n"
			+ "                                                       xsi:schemaLocation=\"http://www.opengis.net/gml/3.3/rgrid http://schemas.opengis.net/gml/3.3/referenceableGrid.xsd\">\n"
			+ "                     <limits>\n" + "                        <GridEnvelope>\n"
			+ "                           <low>0 0 0</low>\n"
			+ "                           <high>3599 1799 184</high>\n" + "                        </GridEnvelope>\n"
			+ "                     </limits>\n" + "                     <axisLabels>Long Lat ansi</axisLabels>\n"
			+ "                     <gmlrgrid:origin>\n"
			+ "                        <Point axisLabels=\"Lat Long ansi\"\n"
			+ "                               gml:id=\"AvgLandTemp-origin\"\n"
			+ "                               srsDimension=\"3\"\n"
			+ "                               srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                               uomLabels=\"degree degree d\">\n"
			+ "                           <pos>89.95 -179.95 145763</pos>\n" + "                        </Point>\n"
			+ "                     </gmlrgrid:origin>\n" + "                     <gmlrgrid:generalGridAxis>\n"
			+ "                        <gmlrgrid:GeneralGridAxis>\n"
			+ "                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                                  srsDimension=\"3\"\n"
			+ "                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                                  uomLabels=\"degree degree d\">0 0.1 0</gmlrgrid:offsetVector>\n"
			+ "                           <gmlrgrid:coefficients/>\n"
			+ "                           <gmlrgrid:gridAxesSpanned>Long</gmlrgrid:gridAxesSpanned>\n"
			+ "                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "                        </gmlrgrid:GeneralGridAxis>\n"
			+ "                     </gmlrgrid:generalGridAxis>\n" + "                     <gmlrgrid:generalGridAxis>\n"
			+ "                        <gmlrgrid:GeneralGridAxis>\n"
			+ "                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                                  srsDimension=\"3\"\n"
			+ "                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                                  uomLabels=\"degree degree d\">-0.1 0 0</gmlrgrid:offsetVector>\n"
			+ "                           <gmlrgrid:coefficients/>\n"
			+ "                           <gmlrgrid:gridAxesSpanned>Lat</gmlrgrid:gridAxesSpanned>\n"
			+ "                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "                        </gmlrgrid:GeneralGridAxis>\n"
			+ "                     </gmlrgrid:generalGridAxis>\n" + "                     <gmlrgrid:generalGridAxis>\n"
			+ "                        <gmlrgrid:GeneralGridAxis>\n"
			+ "                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                                  srsDimension=\"3\"\n"
			+ "                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                                  uomLabels=\"degree degree d\">0 0 1</gmlrgrid:offsetVector>\n"
			+ "                           <gmlrgrid:coefficients>0 29 60 90 121 151 182 213 243 274 304 335 366 394 425 455 486 516 547 578 608 639 669 700 731 759 790 820 851 881 912 943 973 1004 1034 1065 1096 1124 1155 1185 1216 1246 1277 1308 1338 1369 1399 1430 1461 1490 1521 1551 1582 1612 1643 1674 1704 1735 1765 1796 1827 1855 1886 1916 1947 1977 2008 2039 2069 2100 2130 2161 2192 2220 2251 2281 2312 2342 2373 2404 2434 2465 2495 2526 2557 2585 2616 2646 2677 2707 2738 2769 2799 2830 2860 2891 2922 2951 2982 3012 3043 3073 3104 3135 3165 3196 3226 3257 3288 3316 3347 3377 3408 3438 3469 3500 3530 3561 3591 3622 3653 3681 3712 3742 3773 3803 3834 3865 3895 3926 3956 3987 4018 4046 4077 4107 4138 4168 4199 4230 4260 4291 4321 4352 4383 4412 4443 4473 4504 4534 4565 4596 4626 4657 4687 4718 4749 4777 4808 4838 4869 4899 4930 4961 4991 5022 5052 5083 5114 5142 5173 5203 5234 5264 5295 5326 5356 5387 5417 5448 5479 5507 5538 5568 5599</gmlrgrid:coefficients>\n"
			+ "                           <gmlrgrid:gridAxesSpanned>ansi</gmlrgrid:gridAxesSpanned>\n"
			+ "                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "                        </gmlrgrid:GeneralGridAxis>\n"
			+ "                     </gmlrgrid:generalGridAxis>\n"
			+ "                  </gmlrgrid:ReferenceableGridByVectors>\n" + "               </domainSet>\n"
			+ "               <gmlcov:rangeType>\n" + "                  <swe:DataRecord>\n"
			+ "                     <swe:field name=\"Gray\">\n" + "                        <swe:Quantity>\n"
			+ "                           <swe:nilValues>\n" + "                              <swe:NilValues/>\n"
			+ "                           </swe:nilValues>\n" + "                           <swe:uom code=\"10^0\"/>\n"
			+ "                        </swe:Quantity>\n" + "                     </swe:field>\n"
			+ "                  </swe:DataRecord>\n" + "               </gmlcov:rangeType>\n"
			+ "               <wcs:ServiceParameters>\n"
			+ "                  <wcs:CoverageSubtype>ReferenceableGridCoverage</wcs:CoverageSubtype>\n"
			+ "                  <CoverageSubtypeParent xmlns=\"http://www.opengis.net/wcs/2.0\">\n"
			+ "                     <CoverageSubtype>AbstractDiscreteCoverage</CoverageSubtype>\n"
			+ "                     <CoverageSubtypeParent>\n"
			+ "                        <CoverageSubtype>AbstractCoverage</CoverageSubtype>\n"
			+ "                     </CoverageSubtypeParent>\n" + "                  </CoverageSubtypeParent>\n"
			+ "                  <wcs:nativeFormat>application/octet-stream</wcs:nativeFormat>\n"
			+ "               </wcs:ServiceParameters>\n" + "            </wcs:CoverageDescription>\n"
			+ "         </wcs:CoverageDescriptions>\n" + "      </coverage>\n" + "   </coverages>\n" + "</results>";

	public static final String DOUBLE_DESCRIBE_COVERAGE_RESULT = "<results>\n" + "   <coverages>\n"
			+ "      <coverage id=\"AvgLandTemp\">\n"
			+ "         <wcs:CoverageDescriptions xmlns:wcs=\"http://www.opengis.net/wcs/2.0\"\n"
			+ "                                   xmlns:crs=\"http://www.opengis.net/wcs/service-extension/crs/1.0\"\n"
			+ "                                   xmlns:gml=\"http://www.opengis.net/gml/3.2\"\n"
			+ "                                   xmlns:ows=\"http://www.opengis.net/ows/2.0\"\n"
			+ "                                   xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n"
			+ "                                   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
			+ "                                   xsi:schemaLocation=\"http://www.opengis.net/wcs/2.0 http://schemas.opengis.net/wcs/2.0/wcsAll.xsd\">\n"
			+ "            <wcs:CoverageDescription xmlns=\"http://www.opengis.net/gml/3.2\"\n"
			+ "                                     xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n"
			+ "                                     xmlns:gmlcov=\"http://www.opengis.net/gmlcov/1.0\"\n"
			+ "                                     gml:id=\"AvgLandTemp\">\n" + "               <boundedBy>\n"
			+ "                  <Envelope axisLabels=\"Lat Long ansi\"\n"
			+ "                            srsDimension=\"3\"\n"
			+ "                            srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                            uomLabels=\"degree degree d\">\n"
			+ "                     <lowerCorner>-90 -180 145763</lowerCorner>\n"
			+ "                     <upperCorner>90 180 151362</upperCorner>\n" + "                  </Envelope>\n"
			+ "               </boundedBy>\n" + "               <wcs:CoverageId>AvgLandTemp</wcs:CoverageId>\n"
			+ "               <gmlcov:metadata/>\n" + "               <domainSet>\n"
			+ "                  <gmlrgrid:ReferenceableGridByVectors xmlns:gmlrgrid=\"http://www.opengis.net/gml/3.3/rgrid\"\n"
			+ "                                                       dimension=\"3\"\n"
			+ "                                                       gml:id=\"AvgLandTemp-grid\"\n"
			+ "                                                       xsi:schemaLocation=\"http://www.opengis.net/gml/3.3/rgrid http://schemas.opengis.net/gml/3.3/referenceableGrid.xsd\">\n"
			+ "                     <limits>\n" + "                        <GridEnvelope>\n"
			+ "                           <low>0 0 0</low>\n"
			+ "                           <high>3599 1799 184</high>\n" + "                        </GridEnvelope>\n"
			+ "                     </limits>\n" + "                     <axisLabels>Long Lat ansi</axisLabels>\n"
			+ "                     <gmlrgrid:origin>\n"
			+ "                        <Point axisLabels=\"Lat Long ansi\"\n"
			+ "                               gml:id=\"AvgLandTemp-origin\"\n"
			+ "                               srsDimension=\"3\"\n"
			+ "                               srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                               uomLabels=\"degree degree d\">\n"
			+ "                           <pos>89.95 -179.95 145763</pos>\n" + "                        </Point>\n"
			+ "                     </gmlrgrid:origin>\n" + "                     <gmlrgrid:generalGridAxis>\n"
			+ "                        <gmlrgrid:GeneralGridAxis>\n"
			+ "                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                                  srsDimension=\"3\"\n"
			+ "                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                                  uomLabels=\"degree degree d\">0 0.1 0</gmlrgrid:offsetVector>\n"
			+ "                           <gmlrgrid:coefficients/>\n"
			+ "                           <gmlrgrid:gridAxesSpanned>Long</gmlrgrid:gridAxesSpanned>\n"
			+ "                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "                        </gmlrgrid:GeneralGridAxis>\n"
			+ "                     </gmlrgrid:generalGridAxis>\n" + "                     <gmlrgrid:generalGridAxis>\n"
			+ "                        <gmlrgrid:GeneralGridAxis>\n"
			+ "                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                                  srsDimension=\"3\"\n"
			+ "                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                                  uomLabels=\"degree degree d\">-0.1 0 0</gmlrgrid:offsetVector>\n"
			+ "                           <gmlrgrid:coefficients/>\n"
			+ "                           <gmlrgrid:gridAxesSpanned>Lat</gmlrgrid:gridAxesSpanned>\n"
			+ "                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "                        </gmlrgrid:GeneralGridAxis>\n"
			+ "                     </gmlrgrid:generalGridAxis>\n" + "                     <gmlrgrid:generalGridAxis>\n"
			+ "                        <gmlrgrid:GeneralGridAxis>\n"
			+ "                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                                  srsDimension=\"3\"\n"
			+ "                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                                  uomLabels=\"degree degree d\">0 0 1</gmlrgrid:offsetVector>\n"
			+ "                           <gmlrgrid:coefficients>0 29 60 90 121 151 182 213 243 274 304 335 366 394 425 455 486 516 547 578 608 639 669 700 731 759 790 820 851 881 912 943 973 1004 1034 1065 1096 1124 1155 1185 1216 1246 1277 1308 1338 1369 1399 1430 1461 1490 1521 1551 1582 1612 1643 1674 1704 1735 1765 1796 1827 1855 1886 1916 1947 1977 2008 2039 2069 2100 2130 2161 2192 2220 2251 2281 2312 2342 2373 2404 2434 2465 2495 2526 2557 2585 2616 2646 2677 2707 2738 2769 2799 2830 2860 2891 2922 2951 2982 3012 3043 3073 3104 3135 3165 3196 3226 3257 3288 3316 3347 3377 3408 3438 3469 3500 3530 3561 3591 3622 3653 3681 3712 3742 3773 3803 3834 3865 3895 3926 3956 3987 4018 4046 4077 4107 4138 4168 4199 4230 4260 4291 4321 4352 4383 4412 4443 4473 4504 4534 4565 4596 4626 4657 4687 4718 4749 4777 4808 4838 4869 4899 4930 4961 4991 5022 5052 5083 5114 5142 5173 5203 5234 5264 5295 5326 5356 5387 5417 5448 5479 5507 5538 5568 5599</gmlrgrid:coefficients>\n"
			+ "                           <gmlrgrid:gridAxesSpanned>ansi</gmlrgrid:gridAxesSpanned>\n"
			+ "                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "                        </gmlrgrid:GeneralGridAxis>\n"
			+ "                     </gmlrgrid:generalGridAxis>\n"
			+ "                  </gmlrgrid:ReferenceableGridByVectors>\n" + "               </domainSet>\n"
			+ "               <gmlcov:rangeType>\n" + "                  <swe:DataRecord>\n"
			+ "                     <swe:field name=\"Gray\">\n" + "                        <swe:Quantity>\n"
			+ "                           <swe:nilValues>\n" + "                              <swe:NilValues/>\n"
			+ "                           </swe:nilValues>\n" + "                           <swe:uom code=\"10^0\"/>\n"
			+ "                        </swe:Quantity>\n" + "                     </swe:field>\n"
			+ "                  </swe:DataRecord>\n" + "               </gmlcov:rangeType>\n"
			+ "               <wcs:ServiceParameters>\n"
			+ "                  <wcs:CoverageSubtype>ReferenceableGridCoverage</wcs:CoverageSubtype>\n"
			+ "                  <CoverageSubtypeParent xmlns=\"http://www.opengis.net/wcs/2.0\">\n"
			+ "                     <CoverageSubtype>AbstractDiscreteCoverage</CoverageSubtype>\n"
			+ "                     <CoverageSubtypeParent>\n"
			+ "                        <CoverageSubtype>AbstractCoverage</CoverageSubtype>\n"
			+ "                     </CoverageSubtypeParent>\n" + "                  </CoverageSubtypeParent>\n"
			+ "                  <wcs:nativeFormat>application/octet-stream</wcs:nativeFormat>\n"
			+ "               </wcs:ServiceParameters>\n" + "            </wcs:CoverageDescription>\n"
			+ "         </wcs:CoverageDescriptions>\n" + "      </coverage>\n" + "      <coverage id=\"NIR\">\n"
			+ "         <wcs:CoverageDescriptions xmlns:wcs=\"http://www.opengis.net/wcs/2.0\"\n"
			+ "                                   xmlns:crs=\"http://www.opengis.net/wcs/service-extension/crs/1.0\"\n"
			+ "                                   xmlns:gml=\"http://www.opengis.net/gml/3.2\"\n"
			+ "                                   xmlns:ows=\"http://www.opengis.net/ows/2.0\"\n"
			+ "                                   xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n"
			+ "                                   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
			+ "                                   xsi:schemaLocation=\"http://www.opengis.net/wcs/2.0 http://schemas.opengis.net/wcs/2.0/wcsAll.xsd\">\n"
			+ "            <wcs:CoverageDescription xmlns=\"http://www.opengis.net/gml/3.2\"\n"
			+ "                                     xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n"
			+ "                                     xmlns:gmlcov=\"http://www.opengis.net/gmlcov/1.0\"\n"
			+ "                                     gml:id=\"AvgLandTemp\">\n" + "               <boundedBy>\n"
			+ "                  <Envelope axisLabels=\"Lat Long ansi\"\n"
			+ "                            srsDimension=\"3\"\n"
			+ "                            srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                            uomLabels=\"degree degree d\">\n"
			+ "                     <lowerCorner>-90 -180 145763</lowerCorner>\n"
			+ "                     <upperCorner>90 180 151362</upperCorner>\n" + "                  </Envelope>\n"
			+ "               </boundedBy>\n" + "               <wcs:CoverageId>AvgLandTemp</wcs:CoverageId>\n"
			+ "               <gmlcov:metadata/>\n" + "               <domainSet>\n"
			+ "                  <gmlrgrid:ReferenceableGridByVectors xmlns:gmlrgrid=\"http://www.opengis.net/gml/3.3/rgrid\"\n"
			+ "                                                       dimension=\"3\"\n"
			+ "                                                       gml:id=\"AvgLandTemp-grid\"\n"
			+ "                                                       xsi:schemaLocation=\"http://www.opengis.net/gml/3.3/rgrid http://schemas.opengis.net/gml/3.3/referenceableGrid.xsd\">\n"
			+ "                     <limits>\n" + "                        <GridEnvelope>\n"
			+ "                           <low>0 0 0</low>\n"
			+ "                           <high>3599 1799 184</high>\n" + "                        </GridEnvelope>\n"
			+ "                     </limits>\n" + "                     <axisLabels>Long Lat ansi</axisLabels>\n"
			+ "                     <gmlrgrid:origin>\n"
			+ "                        <Point axisLabels=\"Lat Long ansi\"\n"
			+ "                               gml:id=\"AvgLandTemp-origin\"\n"
			+ "                               srsDimension=\"3\"\n"
			+ "                               srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                               uomLabels=\"degree degree d\">\n"
			+ "                           <pos>89.95 -179.95 145763</pos>\n" + "                        </Point>\n"
			+ "                     </gmlrgrid:origin>\n" + "                     <gmlrgrid:generalGridAxis>\n"
			+ "                        <gmlrgrid:GeneralGridAxis>\n"
			+ "                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                                  srsDimension=\"3\"\n"
			+ "                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                                  uomLabels=\"degree degree d\">0 0.1 0</gmlrgrid:offsetVector>\n"
			+ "                           <gmlrgrid:coefficients/>\n"
			+ "                           <gmlrgrid:gridAxesSpanned>Long</gmlrgrid:gridAxesSpanned>\n"
			+ "                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "                        </gmlrgrid:GeneralGridAxis>\n"
			+ "                     </gmlrgrid:generalGridAxis>\n" + "                     <gmlrgrid:generalGridAxis>\n"
			+ "                        <gmlrgrid:GeneralGridAxis>\n"
			+ "                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                                  srsDimension=\"3\"\n"
			+ "                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                                  uomLabels=\"degree degree d\">-0.1 0 0</gmlrgrid:offsetVector>\n"
			+ "                           <gmlrgrid:coefficients/>\n"
			+ "                           <gmlrgrid:gridAxesSpanned>Lat</gmlrgrid:gridAxesSpanned>\n"
			+ "                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "                        </gmlrgrid:GeneralGridAxis>\n"
			+ "                     </gmlrgrid:generalGridAxis>\n" + "                     <gmlrgrid:generalGridAxis>\n"
			+ "                        <gmlrgrid:GeneralGridAxis>\n"
			+ "                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                                  srsDimension=\"3\"\n"
			+ "                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                                  uomLabels=\"degree degree d\">0 0 1</gmlrgrid:offsetVector>\n"
			+ "                           <gmlrgrid:coefficients>0 29 60 90 121 151 182 213 243 274 304 335 366 394 425 455 486 516 547 578 608 639 669 700 731 759 790 820 851 881 912 943 973 1004 1034 1065 1096 1124 1155 1185 1216 1246 1277 1308 1338 1369 1399 1430 1461 1490 1521 1551 1582 1612 1643 1674 1704 1735 1765 1796 1827 1855 1886 1916 1947 1977 2008 2039 2069 2100 2130 2161 2192 2220 2251 2281 2312 2342 2373 2404 2434 2465 2495 2526 2557 2585 2616 2646 2677 2707 2738 2769 2799 2830 2860 2891 2922 2951 2982 3012 3043 3073 3104 3135 3165 3196 3226 3257 3288 3316 3347 3377 3408 3438 3469 3500 3530 3561 3591 3622 3653 3681 3712 3742 3773 3803 3834 3865 3895 3926 3956 3987 4018 4046 4077 4107 4138 4168 4199 4230 4260 4291 4321 4352 4383 4412 4443 4473 4504 4534 4565 4596 4626 4657 4687 4718 4749 4777 4808 4838 4869 4899 4930 4961 4991 5022 5052 5083 5114 5142 5173 5203 5234 5264 5295 5326 5356 5387 5417 5448 5479 5507 5538 5568 5599</gmlrgrid:coefficients>\n"
			+ "                           <gmlrgrid:gridAxesSpanned>ansi</gmlrgrid:gridAxesSpanned>\n"
			+ "                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "                        </gmlrgrid:GeneralGridAxis>\n"
			+ "                     </gmlrgrid:generalGridAxis>\n"
			+ "                  </gmlrgrid:ReferenceableGridByVectors>\n" + "               </domainSet>\n"
			+ "               <gmlcov:rangeType>\n" + "                  <swe:DataRecord>\n"
			+ "                     <swe:field name=\"Gray\">\n" + "                        <swe:Quantity>\n"
			+ "                           <swe:nilValues>\n" + "                              <swe:NilValues/>\n"
			+ "                           </swe:nilValues>\n" + "                           <swe:uom code=\"10^0\"/>\n"
			+ "                        </swe:Quantity>\n" + "                     </swe:field>\n"
			+ "                  </swe:DataRecord>\n" + "               </gmlcov:rangeType>\n"
			+ "               <wcs:ServiceParameters>\n"
			+ "                  <wcs:CoverageSubtype>ReferenceableGridCoverage</wcs:CoverageSubtype>\n"
			+ "                  <CoverageSubtypeParent xmlns=\"http://www.opengis.net/wcs/2.0\">\n"
			+ "                     <CoverageSubtype>AbstractDiscreteCoverage</CoverageSubtype>\n"
			+ "                     <CoverageSubtypeParent>\n"
			+ "                        <CoverageSubtype>AbstractCoverage</CoverageSubtype>\n"
			+ "                     </CoverageSubtypeParent>\n" + "                  </CoverageSubtypeParent>\n"
			+ "                  <wcs:nativeFormat>application/octet-stream</wcs:nativeFormat>\n"
			+ "               </wcs:ServiceParameters>\n" + "            </wcs:CoverageDescription>\n"
			+ "         </wcs:CoverageDescriptions>\n" + "      </coverage>\n" + "   </coverages>\n" + "</results>";

	public static final String QUERY_3_RESPONSE = "<a>\n" + "   <b atr=\"0 0 0 3599 1799 184\">\n"
			+ "      <domainSet xmlns=\"http://www.opengis.net/gml/3.2\"\n"
			+ "                 xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n"
			+ "                 xmlns:gmlcov=\"http://www.opengis.net/gmlcov/1.0\"\n"
			+ "                 xmlns:crs=\"http://www.opengis.net/wcs/service-extension/crs/1.0\"\n"
			+ "                 xmlns:wcs=\"http://www.opengis.net/wcs/2.0\"\n"
			+ "                 xmlns:gml=\"http://www.opengis.net/gml/3.2\"\n"
			+ "                 xmlns:ows=\"http://www.opengis.net/ows/2.0\"\n"
			+ "                 xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n"
			+ "                 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
			+ "         <gmlrgrid:ReferenceableGridByVectors xmlns:gmlrgrid=\"http://www.opengis.net/gml/3.3/rgrid\"\n"
			+ "                                              dimension=\"3\"\n"
			+ "                                              gml:id=\"AvgLandTemp-grid\"\n"
			+ "                                              xsi:schemaLocation=\"http://www.opengis.net/gml/3.3/rgrid http://schemas.opengis.net/gml/3.3/referenceableGrid.xsd\">\n"
			+ "            <limits>\n" + "               <GridEnvelope>\n" + "                  <low>0 0 0</low>\n"
			+ "                  <high>3599 1799 184</high>\n" + "               </GridEnvelope>\n"
			+ "            </limits>\n" + "            <axisLabels>Long Lat ansi</axisLabels>\n"
			+ "            <gmlrgrid:origin>\n" + "               <Point axisLabels=\"Lat Long ansi\"\n"
			+ "                      gml:id=\"AvgLandTemp-origin\"\n" + "                      srsDimension=\"3\"\n"
			+ "                      srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                      uomLabels=\"degree degree d\">\n"
			+ "                  <pos>89.95 -179.95 145763</pos>\n" + "               </Point>\n"
			+ "            </gmlrgrid:origin>\n" + "            <gmlrgrid:generalGridAxis>\n"
			+ "               <gmlrgrid:GeneralGridAxis>\n"
			+ "                  <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                         srsDimension=\"3\"\n"
			+ "                                         srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                         uomLabels=\"degree degree d\">0 0.1 0</gmlrgrid:offsetVector>\n"
			+ "                  <gmlrgrid:coefficients/>\n"
			+ "                  <gmlrgrid:gridAxesSpanned>Long</gmlrgrid:gridAxesSpanned>\n"
			+ "                  <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "               </gmlrgrid:GeneralGridAxis>\n" + "            </gmlrgrid:generalGridAxis>\n"
			+ "            <gmlrgrid:generalGridAxis>\n" + "               <gmlrgrid:GeneralGridAxis>\n"
			+ "                  <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                         srsDimension=\"3\"\n"
			+ "                                         srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                         uomLabels=\"degree degree d\">-0.1 0 0</gmlrgrid:offsetVector>\n"
			+ "                  <gmlrgrid:coefficients/>\n"
			+ "                  <gmlrgrid:gridAxesSpanned>Lat</gmlrgrid:gridAxesSpanned>\n"
			+ "                  <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "               </gmlrgrid:GeneralGridAxis>\n" + "            </gmlrgrid:generalGridAxis>\n"
			+ "            <gmlrgrid:generalGridAxis>\n" + "               <gmlrgrid:GeneralGridAxis>\n"
			+ "                  <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                         srsDimension=\"3\"\n"
			+ "                                         srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                         uomLabels=\"degree degree d\">0 0 1</gmlrgrid:offsetVector>\n"
			+ "                  <gmlrgrid:coefficients>0 29 60 90 121 151 182 213 243 274 304 335 366 394 425 455 486 516 547 578 608 639 669 700 731 759 790 820 851 881 912 943 973 1004 1034 1065 1096 1124 1155 1185 1216 1246 1277 1308 1338 1369 1399 1430 1461 1490 1521 1551 1582 1612 1643 1674 1704 1735 1765 1796 1827 1855 1886 1916 1947 1977 2008 2039 2069 2100 2130 2161 2192 2220 2251 2281 2312 2342 2373 2404 2434 2465 2495 2526 2557 2585 2616 2646 2677 2707 2738 2769 2799 2830 2860 2891 2922 2951 2982 3012 3043 3073 3104 3135 3165 3196 3226 3257 3288 3316 3347 3377 3408 3438 3469 3500 3530 3561 3591 3622 3653 3681 3712 3742 3773 3803 3834 3865 3895 3926 3956 3987 4018 4046 4077 4107 4138 4168 4199 4230 4260 4291 4321 4352 4383 4412 4443 4473 4504 4534 4565 4596 4626 4657 4687 4718 4749 4777 4808 4838 4869 4899 4930 4961 4991 5022 5052 5083 5114 5142 5173 5203 5234 5264 5295 5326 5356 5387 5417 5448 5479 5507 5538 5568 5599</gmlrgrid:coefficients>\n"
			+ "                  <gmlrgrid:gridAxesSpanned>ansi</gmlrgrid:gridAxesSpanned>\n"
			+ "                  <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "               </gmlrgrid:GeneralGridAxis>\n" + "            </gmlrgrid:generalGridAxis>\n"
			+ "         </gmlrgrid:ReferenceableGridByVectors>\n" + "      </domainSet>\n" + "   </b>\n" + "</a>";

	public static final String QUERY_4_RESPONSE = "<a>\n" + "   <b atr=\"yannis\">\n"
			+ "      <domainSet xmlns=\"http://www.opengis.net/gml/3.2\"\n"
			+ "                 xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n"
			+ "                 xmlns:gmlcov=\"http://www.opengis.net/gmlcov/1.0\"\n"
			+ "                 xmlns:crs=\"http://www.opengis.net/wcs/service-extension/crs/1.0\"\n"
			+ "                 xmlns:wcs=\"http://www.opengis.net/wcs/2.0\"\n"
			+ "                 xmlns:gml=\"http://www.opengis.net/gml/3.2\"\n"
			+ "                 xmlns:ows=\"http://www.opengis.net/ows/2.0\"\n"
			+ "                 xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n"
			+ "                 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
			+ "         <gmlrgrid:ReferenceableGridByVectors xmlns:gmlrgrid=\"http://www.opengis.net/gml/3.3/rgrid\"\n"
			+ "                                              dimension=\"3\"\n"
			+ "                                              gml:id=\"AvgLandTemp-grid\"\n"
			+ "                                              xsi:schemaLocation=\"http://www.opengis.net/gml/3.3/rgrid http://schemas.opengis.net/gml/3.3/referenceableGrid.xsd\">\n"
			+ "            <limits>\n" + "               <GridEnvelope>\n" + "                  <low>0 0 0</low>\n"
			+ "                  <high>3599 1799 184</high>\n" + "               </GridEnvelope>\n"
			+ "            </limits>\n" + "            <axisLabels>Long Lat ansi</axisLabels>\n"
			+ "            <gmlrgrid:origin>\n" + "               <Point axisLabels=\"Lat Long ansi\"\n"
			+ "                      gml:id=\"AvgLandTemp-origin\"\n" + "                      srsDimension=\"3\"\n"
			+ "                      srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                      uomLabels=\"degree degree d\">\n"
			+ "                  <pos>89.95 -179.95 145763</pos>\n" + "               </Point>\n"
			+ "            </gmlrgrid:origin>\n" + "            <gmlrgrid:generalGridAxis>\n"
			+ "               <gmlrgrid:GeneralGridAxis>\n"
			+ "                  <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                         srsDimension=\"3\"\n"
			+ "                                         srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                         uomLabels=\"degree degree d\">0 0.1 0</gmlrgrid:offsetVector>\n"
			+ "                  <gmlrgrid:coefficients/>\n"
			+ "                  <gmlrgrid:gridAxesSpanned>Long</gmlrgrid:gridAxesSpanned>\n"
			+ "                  <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "               </gmlrgrid:GeneralGridAxis>\n" + "            </gmlrgrid:generalGridAxis>\n"
			+ "            <gmlrgrid:generalGridAxis>\n" + "               <gmlrgrid:GeneralGridAxis>\n"
			+ "                  <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                         srsDimension=\"3\"\n"
			+ "                                         srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                         uomLabels=\"degree degree d\">-0.1 0 0</gmlrgrid:offsetVector>\n"
			+ "                  <gmlrgrid:coefficients/>\n"
			+ "                  <gmlrgrid:gridAxesSpanned>Lat</gmlrgrid:gridAxesSpanned>\n"
			+ "                  <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "               </gmlrgrid:GeneralGridAxis>\n" + "            </gmlrgrid:generalGridAxis>\n"
			+ "            <gmlrgrid:generalGridAxis>\n" + "               <gmlrgrid:GeneralGridAxis>\n"
			+ "                  <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n"
			+ "                                         srsDimension=\"3\"\n"
			+ "                                         srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n"
			+ "                                         uomLabels=\"degree degree d\">0 0 1</gmlrgrid:offsetVector>\n"
			+ "                  <gmlrgrid:coefficients>0 29 60 90 121 151 182 213 243 274 304 335 366 394 425 455 486 516 547 578 608 639 669 700 731 759 790 820 851 881 912 943 973 1004 1034 1065 1096 1124 1155 1185 1216 1246 1277 1308 1338 1369 1399 1430 1461 1490 1521 1551 1582 1612 1643 1674 1704 1735 1765 1796 1827 1855 1886 1916 1947 1977 2008 2039 2069 2100 2130 2161 2192 2220 2251 2281 2312 2342 2373 2404 2434 2465 2495 2526 2557 2585 2616 2646 2677 2707 2738 2769 2799 2830 2860 2891 2922 2951 2982 3012 3043 3073 3104 3135 3165 3196 3226 3257 3288 3316 3347 3377 3408 3438 3469 3500 3530 3561 3591 3622 3653 3681 3712 3742 3773 3803 3834 3865 3895 3926 3956 3987 4018 4046 4077 4107 4138 4168 4199 4230 4260 4291 4321 4352 4383 4412 4443 4473 4504 4534 4565 4596 4626 4657 4687 4718 4749 4777 4808 4838 4869 4899 4930 4961 4991 5022 5052 5083 5114 5142 5173 5203 5234 5264 5295 5326 5356 5387 5417 5448 5479 5507 5538 5568 5599</gmlrgrid:coefficients>\n"
			+ "                  <gmlrgrid:gridAxesSpanned>ansi</gmlrgrid:gridAxesSpanned>\n"
			+ "                  <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n"
			+ "               </gmlrgrid:GeneralGridAxis>\n" + "            </gmlrgrid:generalGridAxis>\n"
			+ "         </gmlrgrid:ReferenceableGridByVectors>\n" + "      </domainSet>\n" + "   </b>\n" + "</a>";
	
	public static final String QUERY_10_RESPONSE = "<a attr=\"2.2834647\">\n" + 
			"   <coverages>\n" + 
			"      <coverage id=\"AvgLandTemp\">\n" + 
			"         <wcs:CoverageDescriptions xmlns:wcs=\"http://www.opengis.net/wcs/2.0\"\n" + 
			"                                   xmlns:crs=\"http://www.opengis.net/wcs/service-extension/crs/1.0\"\n" + 
			"                                   xmlns:gml=\"http://www.opengis.net/gml/3.2\"\n" + 
			"                                   xmlns:ows=\"http://www.opengis.net/ows/2.0\"\n" + 
			"                                   xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n" + 
			"                                   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + 
			"                                   xsi:schemaLocation=\"http://www.opengis.net/wcs/2.0 http://schemas.opengis.net/wcs/2.0/wcsAll.xsd\">\n" + 
			"            <wcs:CoverageDescription xmlns=\"http://www.opengis.net/gml/3.2\"\n" + 
			"                                     xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n" + 
			"                                     xmlns:gmlcov=\"http://www.opengis.net/gmlcov/1.0\"\n" + 
			"                                     gml:id=\"AvgLandTemp\">\n" + 
			"               <boundedBy>\n" + 
			"                  <Envelope axisLabels=\"Lat Long ansi\"\n" + 
			"                            srsDimension=\"3\"\n" + 
			"                            srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n" + 
			"                            uomLabels=\"degree degree d\">\n" + 
			"                     <lowerCorner>-90 -180 145763</lowerCorner>\n" + 
			"                     <upperCorner>90 180 151362</upperCorner>\n" + 
			"                  </Envelope>\n" + 
			"               </boundedBy>\n" + 
			"               <wcs:CoverageId>AvgLandTemp</wcs:CoverageId>\n" + 
			"               <gmlcov:metadata/>\n" + 
			"               <domainSet>\n" + 
			"                  <gmlrgrid:ReferenceableGridByVectors xmlns:gmlrgrid=\"http://www.opengis.net/gml/3.3/rgrid\"\n" + 
			"                                                       dimension=\"3\"\n" + 
			"                                                       gml:id=\"AvgLandTemp-grid\"\n" + 
			"                                                       xsi:schemaLocation=\"http://www.opengis.net/gml/3.3/rgrid http://schemas.opengis.net/gml/3.3/referenceableGrid.xsd\">\n" + 
			"                     <limits>\n" + 
			"                        <GridEnvelope>\n" + 
			"                           <low>0 0 0</low>\n" + 
			"                           <high>3599 1799 184</high>\n" + 
			"                        </GridEnvelope>\n" + 
			"                     </limits>\n" + 
			"                     <axisLabels>Long Lat ansi</axisLabels>\n" + 
			"                     <gmlrgrid:origin>\n" + 
			"                        <Point axisLabels=\"Lat Long ansi\"\n" + 
			"                               gml:id=\"AvgLandTemp-origin\"\n" + 
			"                               srsDimension=\"3\"\n" + 
			"                               srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n" + 
			"                               uomLabels=\"degree degree d\">\n" + 
			"                           <pos>89.95 -179.95 145763</pos>\n" + 
			"                        </Point>\n" + 
			"                     </gmlrgrid:origin>\n" + 
			"                     <gmlrgrid:generalGridAxis>\n" + 
			"                        <gmlrgrid:GeneralGridAxis>\n" + 
			"                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n" + 
			"                                                  srsDimension=\"3\"\n" + 
			"                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n" + 
			"                                                  uomLabels=\"degree degree d\">0 0.1 0</gmlrgrid:offsetVector>\n" + 
			"                           <gmlrgrid:coefficients/>\n" + 
			"                           <gmlrgrid:gridAxesSpanned>Long</gmlrgrid:gridAxesSpanned>\n" + 
			"                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n" + 
			"                        </gmlrgrid:GeneralGridAxis>\n" + 
			"                     </gmlrgrid:generalGridAxis>\n" + 
			"                     <gmlrgrid:generalGridAxis>\n" + 
			"                        <gmlrgrid:GeneralGridAxis>\n" + 
			"                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n" + 
			"                                                  srsDimension=\"3\"\n" + 
			"                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n" + 
			"                                                  uomLabels=\"degree degree d\">-0.1 0 0</gmlrgrid:offsetVector>\n" + 
			"                           <gmlrgrid:coefficients/>\n" + 
			"                           <gmlrgrid:gridAxesSpanned>Lat</gmlrgrid:gridAxesSpanned>\n" + 
			"                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n" + 
			"                        </gmlrgrid:GeneralGridAxis>\n" + 
			"                     </gmlrgrid:generalGridAxis>\n" + 
			"                     <gmlrgrid:generalGridAxis>\n" + 
			"                        <gmlrgrid:GeneralGridAxis>\n" + 
			"                           <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n" + 
			"                                                  srsDimension=\"3\"\n" + 
			"                                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n" + 
			"                                                  uomLabels=\"degree degree d\">0 0 1</gmlrgrid:offsetVector>\n" + 
			"                           <gmlrgrid:coefficients>0 29 60 90 121 151 182 213 243 274 304 335 366 394 425 455 486 516 547 578 608 639 669 700 731 759 790 820 851 881 912 943 973 1004 1034 1065 1096 1124 1155 1185 1216 1246 1277 1308 1338 1369 1399 1430 1461 1490 1521 1551 1582 1612 1643 1674 1704 1735 1765 1796 1827 1855 1886 1916 1947 1977 2008 2039 2069 2100 2130 2161 2192 2220 2251 2281 2312 2342 2373 2404 2434 2465 2495 2526 2557 2585 2616 2646 2677 2707 2738 2769 2799 2830 2860 2891 2922 2951 2982 3012 3043 3073 3104 3135 3165 3196 3226 3257 3288 3316 3347 3377 3408 3438 3469 3500 3530 3561 3591 3622 3653 3681 3712 3742 3773 3803 3834 3865 3895 3926 3956 3987 4018 4046 4077 4107 4138 4168 4199 4230 4260 4291 4321 4352 4383 4412 4443 4473 4504 4534 4565 4596 4626 4657 4687 4718 4749 4777 4808 4838 4869 4899 4930 4961 4991 5022 5052 5083 5114 5142 5173 5203 5234 5264 5295 5326 5356 5387 5417 5448 5479 5507 5538 5568 5599</gmlrgrid:coefficients>\n" + 
			"                           <gmlrgrid:gridAxesSpanned>ansi</gmlrgrid:gridAxesSpanned>\n" + 
			"                           <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n" + 
			"                        </gmlrgrid:GeneralGridAxis>\n" + 
			"                     </gmlrgrid:generalGridAxis>\n" + 
			"                  </gmlrgrid:ReferenceableGridByVectors>\n" + 
			"               </domainSet>\n" + 
			"               <gmlcov:rangeType>\n" + 
			"                  <swe:DataRecord>\n" + 
			"                     <swe:field name=\"Gray\">\n" + 
			"                        <swe:Quantity>\n" + 
			"                           <swe:nilValues>\n" + 
			"                              <swe:NilValues/>\n" + 
			"                           </swe:nilValues>\n" + 
			"                           <swe:uom code=\"10^0\"/>\n" + 
			"                        </swe:Quantity>\n" + 
			"                     </swe:field>\n" + 
			"                  </swe:DataRecord>\n" + 
			"               </gmlcov:rangeType>\n" + 
			"               <wcs:ServiceParameters>\n" + 
			"                  <wcs:CoverageSubtype>ReferenceableGridCoverage</wcs:CoverageSubtype>\n" + 
			"                  <CoverageSubtypeParent xmlns=\"http://www.opengis.net/wcs/2.0\">\n" + 
			"                     <CoverageSubtype>AbstractDiscreteCoverage</CoverageSubtype>\n" + 
			"                     <CoverageSubtypeParent>\n" + 
			"                        <CoverageSubtype>AbstractCoverage</CoverageSubtype>\n" + 
			"                     </CoverageSubtypeParent>\n" + 
			"                  </CoverageSubtypeParent>\n" + 
			"                  <wcs:nativeFormat>application/octet-stream</wcs:nativeFormat>\n" + 
			"               </wcs:ServiceParameters>\n" + 
			"            </wcs:CoverageDescription>\n" + 
			"         </wcs:CoverageDescriptions>\n" + 
			"      </coverage>\n" + 
			"   </coverages>\n" + 
			"</a>";
	
	public static final String QUERY_15_RESPONSE = "<p attr=\"2.2834647\">\n" + 
			"   <a>text</a>\n" + 
			"   <results>\n" + 
			"      <coverages>\n" + 
			"         <coverage id=\"AvgLandTemp\">\n" + 
			"            <wcs:CoverageDescriptions xmlns:wcs=\"http://www.opengis.net/wcs/2.0\"\n" + 
			"                                      xmlns:crs=\"http://www.opengis.net/wcs/service-extension/crs/1.0\"\n" + 
			"                                      xmlns:gml=\"http://www.opengis.net/gml/3.2\"\n" + 
			"                                      xmlns:ows=\"http://www.opengis.net/ows/2.0\"\n" + 
			"                                      xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n" + 
			"                                      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + 
			"                                      xsi:schemaLocation=\"http://www.opengis.net/wcs/2.0 http://schemas.opengis.net/wcs/2.0/wcsAll.xsd\">\n" + 
			"               <wcs:CoverageDescription xmlns=\"http://www.opengis.net/gml/3.2\"\n" + 
			"                                        xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n" + 
			"                                        xmlns:gmlcov=\"http://www.opengis.net/gmlcov/1.0\"\n" + 
			"                                        gml:id=\"AvgLandTemp\">\n" + 
			"                  <boundedBy>\n" + 
			"                     <Envelope axisLabels=\"Lat Long ansi\"\n" + 
			"                               srsDimension=\"3\"\n" + 
			"                               srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n" + 
			"                               uomLabels=\"degree degree d\">\n" + 
			"                        <lowerCorner>-90 -180 145763</lowerCorner>\n" + 
			"                        <upperCorner>90 180 151362</upperCorner>\n" + 
			"                     </Envelope>\n" + 
			"                  </boundedBy>\n" + 
			"                  <wcs:CoverageId>AvgLandTemp</wcs:CoverageId>\n" + 
			"                  <gmlcov:metadata/>\n" + 
			"                  <domainSet>\n" + 
			"                     <gmlrgrid:ReferenceableGridByVectors xmlns:gmlrgrid=\"http://www.opengis.net/gml/3.3/rgrid\"\n" + 
			"                                                          dimension=\"3\"\n" + 
			"                                                          gml:id=\"AvgLandTemp-grid\"\n" + 
			"                                                          xsi:schemaLocation=\"http://www.opengis.net/gml/3.3/rgrid http://schemas.opengis.net/gml/3.3/referenceableGrid.xsd\">\n" + 
			"                        <limits>\n" + 
			"                           <GridEnvelope>\n" + 
			"                              <low>0 0 0</low>\n" + 
			"                              <high>3599 1799 184</high>\n" + 
			"                           </GridEnvelope>\n" + 
			"                        </limits>\n" + 
			"                        <axisLabels>Long Lat ansi</axisLabels>\n" + 
			"                        <gmlrgrid:origin>\n" + 
			"                           <Point axisLabels=\"Lat Long ansi\"\n" + 
			"                                  gml:id=\"AvgLandTemp-origin\"\n" + 
			"                                  srsDimension=\"3\"\n" + 
			"                                  srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n" + 
			"                                  uomLabels=\"degree degree d\">\n" + 
			"                              <pos>89.95 -179.95 145763</pos>\n" + 
			"                           </Point>\n" + 
			"                        </gmlrgrid:origin>\n" + 
			"                        <gmlrgrid:generalGridAxis>\n" + 
			"                           <gmlrgrid:GeneralGridAxis>\n" + 
			"                              <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n" + 
			"                                                     srsDimension=\"3\"\n" + 
			"                                                     srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n" + 
			"                                                     uomLabels=\"degree degree d\">0 0.1 0</gmlrgrid:offsetVector>\n" + 
			"                              <gmlrgrid:coefficients/>\n" + 
			"                              <gmlrgrid:gridAxesSpanned>Long</gmlrgrid:gridAxesSpanned>\n" + 
			"                              <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n" + 
			"                           </gmlrgrid:GeneralGridAxis>\n" + 
			"                        </gmlrgrid:generalGridAxis>\n" + 
			"                        <gmlrgrid:generalGridAxis>\n" + 
			"                           <gmlrgrid:GeneralGridAxis>\n" + 
			"                              <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n" + 
			"                                                     srsDimension=\"3\"\n" + 
			"                                                     srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n" + 
			"                                                     uomLabels=\"degree degree d\">-0.1 0 0</gmlrgrid:offsetVector>\n" + 
			"                              <gmlrgrid:coefficients/>\n" + 
			"                              <gmlrgrid:gridAxesSpanned>Lat</gmlrgrid:gridAxesSpanned>\n" + 
			"                              <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n" + 
			"                           </gmlrgrid:GeneralGridAxis>\n" + 
			"                        </gmlrgrid:generalGridAxis>\n" + 
			"                        <gmlrgrid:generalGridAxis>\n" + 
			"                           <gmlrgrid:GeneralGridAxis>\n" + 
			"                              <gmlrgrid:offsetVector axisLabels=\"Lat Long ansi\"\n" + 
			"                                                     srsDimension=\"3\"\n" + 
			"                                                     srsName=\"http://localhost:9090/def/crs-compound?1=http://localhost:9090/def/crs/EPSG/0/4326&amp;2=http://localhost:9090/def/crs/OGC/0/AnsiDate\"\n" + 
			"                                                     uomLabels=\"degree degree d\">0 0 1</gmlrgrid:offsetVector>\n" + 
			"                              <gmlrgrid:coefficients>0 29 60 90 121 151 182 213 243 274 304 335 366 394 425 455 486 516 547 578 608 639 669 700 731 759 790 820 851 881 912 943 973 1004 1034 1065 1096 1124 1155 1185 1216 1246 1277 1308 1338 1369 1399 1430 1461 1490 1521 1551 1582 1612 1643 1674 1704 1735 1765 1796 1827 1855 1886 1916 1947 1977 2008 2039 2069 2100 2130 2161 2192 2220 2251 2281 2312 2342 2373 2404 2434 2465 2495 2526 2557 2585 2616 2646 2677 2707 2738 2769 2799 2830 2860 2891 2922 2951 2982 3012 3043 3073 3104 3135 3165 3196 3226 3257 3288 3316 3347 3377 3408 3438 3469 3500 3530 3561 3591 3622 3653 3681 3712 3742 3773 3803 3834 3865 3895 3926 3956 3987 4018 4046 4077 4107 4138 4168 4199 4230 4260 4291 4321 4352 4383 4412 4443 4473 4504 4534 4565 4596 4626 4657 4687 4718 4749 4777 4808 4838 4869 4899 4930 4961 4991 5022 5052 5083 5114 5142 5173 5203 5234 5264 5295 5326 5356 5387 5417 5448 5479 5507 5538 5568 5599</gmlrgrid:coefficients>\n" + 
			"                              <gmlrgrid:gridAxesSpanned>ansi</gmlrgrid:gridAxesSpanned>\n" + 
			"                              <gmlrgrid:sequenceRule axisOrder=\"+1\">Linear</gmlrgrid:sequenceRule>\n" + 
			"                           </gmlrgrid:GeneralGridAxis>\n" + 
			"                        </gmlrgrid:generalGridAxis>\n" + 
			"                     </gmlrgrid:ReferenceableGridByVectors>\n" + 
			"                  </domainSet>\n" + 
			"                  <gmlcov:rangeType>\n" + 
			"                     <swe:DataRecord>\n" + 
			"                        <swe:field name=\"Gray\">\n" + 
			"                           <swe:Quantity>\n" + 
			"                              <swe:nilValues>\n" + 
			"                                 <swe:NilValues/>\n" + 
			"                              </swe:nilValues>\n" + 
			"                              <swe:uom code=\"10^0\"/>\n" + 
			"                           </swe:Quantity>\n" + 
			"                        </swe:field>\n" + 
			"                     </swe:DataRecord>\n" + 
			"                  </gmlcov:rangeType>\n" + 
			"                  <wcs:ServiceParameters>\n" + 
			"                     <wcs:CoverageSubtype>ReferenceableGridCoverage</wcs:CoverageSubtype>\n" + 
			"                     <CoverageSubtypeParent xmlns=\"http://www.opengis.net/wcs/2.0\">\n" + 
			"                        <CoverageSubtype>AbstractDiscreteCoverage</CoverageSubtype>\n" + 
			"                        <CoverageSubtypeParent>\n" + 
			"                           <CoverageSubtype>AbstractCoverage</CoverageSubtype>\n" + 
			"                        </CoverageSubtypeParent>\n" + 
			"                     </CoverageSubtypeParent>\n" + 
			"                     <wcs:nativeFormat>application/octet-stream</wcs:nativeFormat>\n" + 
			"                  </wcs:ServiceParameters>\n" + 
			"               </wcs:CoverageDescription>\n" + 
			"            </wcs:CoverageDescriptions>\n" + 
			"         </coverage>\n" + 
			"      </coverages>\n" + 
			"   </results>\n" + 
			"</p>";
}
