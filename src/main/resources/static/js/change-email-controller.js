'use strict';

app.controller('ChangeEmailCtrl', function ($scope, $rootScope, $uibModalInstance, Connector, currentEmail) {

    $scope.currentEmail = currentEmail;
    $scope.changeEmailWrapper = {};
    $scope.emailAvailable = true;
    $scope.changeEmailSubmitDisable = false;

    $scope.ok = function () {
        $scope.changeEmailSubmitDisable = true;
        return Connector.changeEmail($scope.changeEmailWrapper, createAuthorizationTokenHeader())
            .then(
                function (result) {
                    $scope.changeEmailSubmitDisable = false;
                    $uibModalInstance.close(result);
                    return result;
                },
                function (errResponse) {
                    $scope.changeEmailSubmitDisable = false;
                    console.error(JSON.stringify(errResponse));
                    $rootScope.authExceptionCheck(errResponse);
                    $uibModalInstance.close(errResponse.message);
                    return null;
                }
            );
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    var resizeTimer;
    $scope.checkEmail = function (email) {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function () {
            if(typeof email !== 'undefined'){
                Connector.isEmailRegistered(email).then(
                    function (result) {
                        $scope.emailAvailable = !result;
                    },
                    function () {
                        console.error('Error while fetching isEmailRegistered');
                    }
                )

            }
        }, 1000);
    };
});