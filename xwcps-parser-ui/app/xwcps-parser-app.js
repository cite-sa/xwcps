var parserApp = angular.module('xWCPSParserApp', ['ngResource', 'hljs']);

parserApp.config(function (hljsServiceProvider) {
	hljsServiceProvider.setOptions({
		// replace tab with 4 spaces
		tabReplace: '    '
	});
});

parserApp.directive("selectorClickHandler", function () {
	return {
		link: function (scope, element, attrs) {

			$(element).click(function () {
				//scope.queries.default.query = $(this).data("description");
				scope.queries.default.description = $(this).find("a").attr("title");
				editor.setValue($(this).data("description"));
				scope.$apply();
			});
		}
	}
});

parserApp.directive('resizeQueryForm', function ($window) {
	return function ($scope) {
		$scope.queryDropdownResize = function () {
			var queryInputWidth = angular.element(document.querySelector("#queryInput")).width();
			var queriesDropdownToggleWidth = angular.element(document.querySelector("#queries-dropdown-toggle")).width();
			angular.element(document.querySelectorAll('.query-a')).width(queryInputWidth + queriesDropdownToggleWidth + 10);
		};
		$scope.queryDropdownResize();
		return angular.element($window).bind('resize', function () {
			$scope.queryDropdownResize();
			return $scope.$apply();
		});
	};
});


parserApp.controller("xWCPSExecutorController", function ($scope, $timeout, $http, $resource) {
	$scope.loader = true;
	$scope.response = {};
	$scope.response.result = undefined;
	$scope.response.error = undefined;

	$scope.execute = function () {
		$scope.loader = false;
		$scope.response.result = undefined;
		$scope.response.error = undefined;

		// var url = "http://earthserver-devel.vhosts.cite.gr/xwcps-application/parser/query";
		var url = "http://localhost:8084/xwcps/parser/query";

		$http.get(url, {
			params: {
				q: editor.getValue()
			},
			headers: {
				accept: "application/json"
			}
		}).then(function (data) {
			console.log(data);
			$scope.response.result = data.data;
			console.log($scope.response.result)
		}, function (error) {
			$scope.response.error = error;
			console.log(error);
		});

	};

	$scope.prettifyXml = function (value) {
		if (value == null) {
			return undefined;
		}
		return vkbeautify.xml(value, 4);
	};

	$scope.getSize = function (result) {
		var size = 0;

		if (result == undefined) {
			return 0;
		}

		if (result.aggregatedValue) {
			if (result.aggregatedValue == "<results></results>") {
				return 0;
			}
			++size;
		}
		if (result.mixedValues) {
			size += result.mixedValues.length;
		}

		return size;
	};

	$scope.queries = {};

	/*$scope.queries.all = [
		{
			description: "Retrieve coverages, with solar longitude greater than 86.0122, " +
			"and band combination of BD1435,BD1500_2 and BD1900_2 for ICE detection",

			query: "for data in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage \n" +
			"where describeCoverage(data)//gml:cat_solar_longitude[text()>86.0122] \n" +
			"return encode(\n" +
			"{ red:(int)(255 / (1 - (1.395 / ((1 - ((data.band_61 - data.band_57)/(data.band_72 - data.band_57))) * 1.370 + ((data.band_61 - data.band_57)/(data.band_72 - data.band_57)) * 1.470)))); \n" +
			"  green: (int)(255 / (1 - (1.525 / ((1 - ((data.band_80 - data.band_57)/(data.band_124 - data.band_57))) * 1.367 + ((data.band_80 - data.band_57)/(data.band_124 - data.band_57)) * 1.808)))); \n" +
			"  blue: (int)(255/ (0.5 * (1 - (1.930 / ((1 - ((data.band_142 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_142 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))) * 0.5 * (1 - (1.985 / ((1 - ((data.band_151 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_151 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))))); \n" +
			"  alpha: (data.band_100 != 65535) * 255 \n" +
			"}, \"png\", \"nodata=null\")"
		},
		{
			description: "List the coverage descriptions from http://access.planetserver.eu:8080/rasdaman/ows",

			query: "for data in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage return describeCoverage(data)"
		},
		{
			description: "List the coverage descriptions from http://access.planetserver.eu:8080/rasdaman/ows," +
			" where solar longitude greater than 86.0122 ",

			query: "for data in //coverage \nreturn describeCoverage(data)//gml:cat_solar_longitude[text()>86.0122]"
		},
		{
			description: "List the solar longitudes greater than 86.0122, wrapped by 'p' element",

			query: "for data in //coverage return <p> describeCoverage(data)//gml:cat_solar_longitude[text()>86.0122] </p>"
		},
		{
			description: "An example of HTML-like result formatting",

			query: "for data in //coverage \n return <div><p>describeCoverage(data)//gml:adding_target/text()</p> " +
			"<p>describeCoverage(data)//gml:cat_solar_longitude/text()</p></div>"
		}];*/
	
	$scope.queries.all = [
				  		// {
				  		// 	description: "Retrieve coverages, with solar longitude greater than 86.0122, " +
				  		// 	"and band combination of BD1435,BD1500_2 and BD1900_2 for ICE detection",

				  		// 	query: "for $c in ( CCI_V2_monthly_rrs_670 ) return metadata($c)"
				  		// },
				  		// {
				  		// 	description: "List the coverage descriptions from http://access.planetserver.eu:8080/rasdaman/ows",

				  		// 	query: " for $c in /server/coverage return metadata($c)"
				  		// },
				  		// {
				  		// 	description: "List the coverage descriptions from http://access.planetserver.eu:8080/rasdaman/ows," +
				  		// 	" where solar longitude greater than 86.0122 ",

				  		// 	query: "for $c in /server[@endpoint='https://rsg.pml.ac.uk/rasdaman/ows']/coverage return metadata($c)"
				  		// },
				  		// {
				  		// 	description: "List the solar longitudes greater than 86.0122, wrapped by 'p' element",

				  		// 	query: " for $c in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage where metadata($c)//gml:cat_solar_longitude[text()<86.0122] return metadata($c)"
				  		// },
				  		// {
				  		// 	description: "An example of HTML-like result formatting",

				  		// 	query: "for $c in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage where metadata($c)//gml:cat_solar_longitude[text()<86.0122] return metadata($c)//gml:cat_solar_longitude"
				  		// },
				  		// {
				  		// 	description: "An example of HTML-like result formatting",

				  		// 	query: "for data in (frt0000cc22_07_if165l_trr3) return mixed(encode( { red: (int)(255 / (max((data.band_233 != 65535) * data.band_233) - min(data.band_233))) * (data.band_233 - min(data.band_233)); green: (int)(255 / (max((data.band_13 != 65535) * data.band_13) - min(data.band_13))) * (data.band_13 - min(data.band_13)); blue: (int)(255 / (max((data.band_78 != 65535) * data.band_78) - min(data.band_78))) * (data.band_78 - min(data.band_78)) ; alpha: (data.band_100 != 65535) * 255}, \"png\", \"nodata=null\"), metadata(data))"
						//   }
						{
							description: "Filter MEEO by low and return high element's text",
							query: "for $c in *@MEEO \n" +
									"where $c:://GridEnvelope[low/text()='-15690 -53750 0'] \n" +
									"return $c:://high"
						},
						{
							description: "Filter PlanetServer by Envelope lowerCorner and return Envelope element",
							query: "for $c in *@PS \n" +
									"where $c:://Envelope[lowerCorner/text()='-9170.5952 -1309984.1507'] \n" +
									"return $c:://Envelope"
						},
						{
							description: "Retrieve coverages, with solar longitude greater than 86.0122, " +
							"and band combination of BD1435,BD1500_2 and BD1900_2 for ICE detection",
				
							query: "for $c in mslp \n" +
									"return $c::"
						},
						{
							description: "Retrieve coverages, with solar longitude greater than 86.0122, " +
							"and band combination of BD1435,BD1500_2 and BD1900_2 for ICE detection",
				
							query: "for $c in precipitation@ECMWF  \n" +
									"return $c::"
						},
						{
							description: "List the coverage descriptions from all WCS endpoints",
				
							query: "for $c in *@ECMWF \n" +
									"return $c:://boundedBy/Envelope/@srsName"
						},
						{
							description: "List the coverage descriptions from ECMWF",
				
							query: "for $c in *@ECMWF \n" +
									"return $c::"
						},
						/*{
							description: "List the solar longitudes greater than 86.0122, wrapped by 'p' element",
				
							query: "for $c in *@ECMWF \n" +
									"where $c::/*//*[local-name()='lowerCorner'][text()='-90.25 -180.25 138061.875'] \n" +
									"return $c::"
						},*/
						{
							description: "List the lowerCorner elements of all ECMWF coverages",
				
							query: "for $c in *@ECMWF \n" +
									"return $c:://lowerCorner"
						},
						{
							description: "Filter by RectifiedGrid dimension = 3 and return the Point elements",
							query: "for $c in *@ECMWF \n" +
									"where $c:://RectifiedGrid[@dimension=3] \n" +
									"return $c:://Point"
						},
					{
							description: "An example of HTML-like result formatting",
							query: "for $c in CCI_V2_monthly_chlor_a@PML, precipitation@ECMWF, L8_B10_32631_30@MEEO \n" +
						   "return <div>$c:://boundedBy</div>"
						},
					{
						description: "Order by coverage id ascending query",
						query: "for $c in *@ECMWF \n" + 
							   "orderby $c:://wcs:CoverageId/text() asc \n" +
							   "return $c:://wcs:CoverageId"
					},
					{
							description: "Mixed query",
							query:  "for $c in CCI_V2_monthly_chlor_a \n" +
							"return mixed(encode ($c[ansi(\"2001-07-31T23:59:00\")] * 1000 , \"png\"), $c::)"
						}
						/*{
							description: "An example of HTML-like result formatting",
				
							query: "for data in frt00004a4b_07_if163l_trr3 \n" +
							"return mixed(encode( { red: (int)((255 / (max((data.band_233 != 65535) * data.band_233) - min(data.band_233))) * (data.band_233 - min(data.band_233))); green: (int)((255 / (max((data.band_13 != 65535) * data.band_13) - min(data.band_13))) * (data.band_13 - min(data.band_13))); blue: (int)((255 / (max((data.band_78 != 65535) * data.band_78) - min(data.band_78))) * (data.band_78 - min(data.band_78))) ; alpha: (data.band_100 != 65535) * 255}, \"png\", \"nodata=null\"), metadata(data))"
							//query: "for data in (frt0000cc22_07_if165l_trr3) return mixed(encode( { red: (int)(255 / (max((data.band_233 != 65535) * data.band_233) - min(data.band_233))) * (data.band_233 - min(data.band_233)); green: (int)(255 / (max((data.band_13 != 65535) * data.band_13) - min(data.band_13))) * (data.band_13 - min(data.band_13)); blue: (int)(255 / (max((data.band_78 != 65535) * data.band_78) - min(data.band_78))) * (data.band_78 - min(data.band_78)) ; alpha: (data.band_100 != 65535) * 255}, \"png\", \"nodata=null\"), metadata(data))"
						}*/
					];

	$scope.queries.default = {};
	$scope.queries.default.query = $scope.queries.all[0].query;
	$scope.queries.default.description = $scope.queries.all[0].description;

});