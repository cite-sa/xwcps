/**
 * Created by ikavvouras on 11/4/2016.
 */

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
				scope.queries.default = $(this).data("description");
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
	// var QueryResponse = $resource("http://localhost:9292/parser/query");
	$scope.loader = true;
	$scope.response = {};
	$scope.response.result = undefined;
	$scope.response.error = undefined;

	$scope.execute = function () {
		$scope.loader = false;
		$scope.response.result = undefined;
		$scope.response.error = undefined;

		$http.jsonp("http://192.168.32.87:9292/parser/queryP", {
			params: {
				q: $scope.queries.default,
				callback: "JSON_CALLBACK"
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
			++size;
		}
		if (result.mixedValues) {
			size += result.mixedValues.length;
		}

		return size;
	};

	$scope.queries = {};
	$scope.queries.default =
		/*"for data in //coverage return describeCoverage(data)//gml:cat_solar_longitude[text()>86.0122]";*/
		"for data in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage " +
		"where describeCoverage(data)//gml:cat_solar_longitude[text()>86.0122] return encode( \n" +
		"{ red:(int)(255 / (1 - (1.395 / ((1 - ((data.band_61 - data.band_57)/(data.band_72 - data.band_57))) * 1.370 + ((data.band_61 - data.band_57)/(data.band_72 - data.band_57)) * 1.470)))); \n" +
		"green: (int)(255 / (1 - (1.525 / ((1 - ((data.band_80 - data.band_57)/(data.band_124 - data.band_57))) * 1.367 + ((data.band_80 - data.band_57)/(data.band_124 - data.band_57)) * 1.808)))); \n" +
		"blue: (int)(255/ (0.5 * (1 - (1.930 / ((1 - ((data.band_142 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_142 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))) * 0.5 * (1 - (1.985 / ((1 - ((data.band_151 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_151 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))))); \n" +
		"alpha: (data.band_100 != 65535) * 255 }, \"png\", \"nodata=null\")";


	$scope.queries.all = [
		"for data in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage " +
		"where describeCoverage(data)//gml:cat_solar_longitude[text()>86.0122] return encode( \n" +
		"{ red:(int)(255 / (1 - (1.395 / ((1 - ((data.band_61 - data.band_57)/(data.band_72 - data.band_57))) * 1.370 + ((data.band_61 - data.band_57)/(data.band_72 - data.band_57)) * 1.470)))); \n" +
		"green: (int)(255 / (1 - (1.525 / ((1 - ((data.band_80 - data.band_57)/(data.band_124 - data.band_57))) * 1.367 + ((data.band_80 - data.band_57)/(data.band_124 - data.band_57)) * 1.808)))); \n" +
		"blue: (int)(255/ (0.5 * (1 - (1.930 / ((1 - ((data.band_142 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_142 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))) * 0.5 * (1 - (1.985 / ((1 - ((data.band_151 - data.band_130)/(data.band_163 - data.band_130))) * 1.850 + ((data.band_151 - data.band_130)/(data.band_163 - data.band_130)) * 2.067))))); \n" +
		"alpha: (data.band_100 != 65535) * 255 }, \"png\", \"nodata=null\")",
		"for data in /server[@endpoint='http://access.planetserver.eu:8080/rasdaman/ows']/coverage return describeCoverage(data)",
		"for data in //coverage return describeCoverage(data)//gml:cat_solar_longitude[text()>86.0122]",
		"for data in //coverage return <p> describeCoverage(data)//gml:cat_solar_longitude[text()>86.0122] </p>",
		"for data in //coverage return <div><p>describeCoverage(data)//gml:adding_target/text()</p> " +
		"<p>describeCoverage(data)//gml:cat_solar_longitude/text()</p></div>"];
});



