'use strict';

app.controller('VerifyEmailController', function ($scope, $rootScope, $http, $stateParams, $location, Connector, AlertsService) {

    $scope.token = $stateParams.token;
    $rootScope.showCarousel = false;

    $scope.verify = function (token) {
        return Connector.verify(token)
            .then(
                function (response) {
                    $rootScope.deAuthorize();
                    AlertsService.createConfirmationPage(response);
                    return response;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $scope.response = {};
                    $scope.response.title = "Error";
                    $scope.response.body = errResponse.message;
                    AlertsService.createConfirmationPage($scope.response);
                }
            )
    };
    $scope.verify($scope.token);
});