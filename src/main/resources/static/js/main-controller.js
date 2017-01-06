'use strict';

app.controller('MainController', function ($scope, $rootScope, $http, $templateCache, Connector) {

    $scope.chartSelectionsList = null;
    $scope.activeChartSelection = {};
    $scope.chartWrapper = null;

    $scope.getGeneralSelectionsList = function () {
        Connector.getGeneralSelectionsList()
            .then(
                function (chartSelectionsList) {
                    $scope.chartSelectionsList = chartSelectionsList;
                    $scope.activeChartSelection = $scope.chartSelectionsList[0];
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.data.message);
                }
            );
    };
    $scope.getGeneralSelectionsList();

    $scope.getDefaultDataTable = function () {
        return Connector.getGeneralDefaultDataTable()
            .then(
                function (defaultChartWrapper) {
                    return defaultChartWrapper;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.data.message);
                }
            )
    };

    $scope.getDataTable = function (chartRequestWrapper) {
        return Connector.getGeneralDataTable(chartRequestWrapper)
            .then(
                function (chartWrapper) {
                    return chartWrapper;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.data.message);
                }
            )
    };
});