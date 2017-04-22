'use strict';

app.controller('UserHomeController', function ($scope, $rootScope, $http, $templateCache, $location, $state, Connector) {
    console.log('UserHomeController');
    $scope.chartSelectionsList = null;
    $scope.activeChartSelection = {};
    $scope.chartWrapper = {};
    $scope.datePicker = {};
    $scope.datePicker.datePickerForm = {};
    $scope.datePickerSubmitDisable = false;
    $rootScope.authCheck();
    chartAuth = $scope.isAutorized;
    $rootScope.showCarousel = false;
    $rootScope.activeTab = 0;
    $scope.selectionDropdownDivider = false;

    $scope.getUserSelectionsList = function () {
        Connector.getUserSelectionsList()
            .then(
                function (chartSelectionsList) {
                    $scope.chartSelectionsList = chartSelectionsList;
                    $scope.activeChartSelection = $scope.chartSelectionsList[0];
                    angular.forEach($scope.chartSelectionsList, function(selection) {
                        if(selection.datePicker === true)
                            $scope.selectionDropdownDivider = true;
                    });
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.message);
                    $rootScope.authExceptionCheck(errResponse);
                }
            );
    };
    if($scope.isAutorized)
        $scope.getUserSelectionsList();

    $scope.getDefaultDataTable = function () {
        return Connector.getUserDefaultDataTable(createAuthorizationTokenHeader())
            .then(
                function (defaultChartWrapper) {
                    return defaultChartWrapper;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.message);
                    $rootScope.authExceptionCheck(errResponse);
                    return null;
                }
            );
    };

    $scope.getDataTable = function (chartRequestWrapper) {
        $scope.datePickerSubmitDisable = true;
        return Connector.getUserDataTable(chartRequestWrapper, createAuthorizationTokenHeader())
            .then(
                function (chartWrapper) {
                    $scope.datePickerSubmitDisable = false;
                    return chartWrapper;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.message);
                    $scope.datePickerSubmitDisable = false;
                    $rootScope.authExceptionCheck(errResponse);
                    return null;
                }
            )
    };
});