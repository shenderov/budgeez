'use strict';

app.controller('ConfirmController', function ($scope, $rootScope, $http, $stateParams, $location, Connector, AlertsService) {

    $scope.token = $stateParams.token;
    $rootScope.showCarousel = false;

    $scope.confirm = function (token) {
        return Connector.confirm(token)
            .then(
                function (response) {
                    AlertsService.createConfirmationPage(response);
                    return response;
                },
                function (errResponse) {
                    AlertsService.createConfirmationPage(errResponse);
                }
            )
    };
    $scope.confirm($scope.token);
});