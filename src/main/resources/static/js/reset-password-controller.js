'use strict';

app.controller('ResetPasswordController', function ($scope, $stateParams, Connector, AlertsService) {

    $scope.resetPasswordWrapper = {};
    $scope.resetPasswordWrapper.token = $stateParams.token;
    $scope.resetPasswordSubmitDisable = false;

    $scope.ok = function () {
        $scope.resetPasswordSubmitDisable = true;
        return Connector.resetPassword($scope.resetPasswordWrapper)
            .then(
                function (result) {
                    $scope.resetPasswordSubmitDisable = false;
                    AlertsService.createConfirmationPage(result);
                    return result;
                },
                function (result) {
                    $scope.resetPasswordSubmitDisable = false;
                    console.error(JSON.stringify(errResponse));
                    AlertsService.createConfirmationPage(result);
                    return null;
                }
            );
    };

});