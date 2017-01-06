'use strict';

app.controller('UserHomeController', function ($scope, $rootScope, $http, $templateCache, $location, $state, Connector) {
    console.log("User");

    // if($scope.isAutorized == false){
    //     $location.path('/');
    // }

    $scope.chartSelectionsList = null;
    $scope.activeChartSelection = {};
    $scope.chartWrapper = {};

    $scope.getUserSelectionsList = function () {
        Connector.getUserSelectionsList()
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
    $scope.getUserSelectionsList();

    $scope.getDefaultDataTable = function () {
        return Connector.getUserDefaultDataTable(createAuthorizationTokenHeader())
            .then(
                function (defaultChartWrapper) {
                    return defaultChartWrapper;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.data.message);
                    return null;
                }
            );
    };

    $scope.getDataTable = function (chartRequestWrapper) {
        return Connector.getUserDataTable(chartRequestWrapper, createAuthorizationTokenHeader())
            .then(
                function (chartWrapper) {
                    return chartWrapper;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.data.message);
                    return null;
                }
            )
    };
});