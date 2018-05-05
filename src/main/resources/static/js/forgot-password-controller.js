'use strict';

app.controller('ForgotPasswordCtrl', function ($scope, $rootScope, $uibModalInstance, Connector, AlertsService) {

    $scope.forgotPasswordWrapper = {};
    $scope.forgotPasswordSubmitDisable = false;
    $scope.emailRegistered = true;

    $scope.ok = function () {
        $scope.forgotPasswordSubmitDisable = true;
        return Connector.forgotPassword($scope.forgotPasswordWrapper.email)
            .then(
                function (response) {
                    $scope.forgotPasswordSubmitDisable = false;
                    $uibModalInstance.close(response);
                    return response;
                },
                function (errResponse) {
                    $scope.forgotPasswordSubmitDisable = false;
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
        $scope.forgotPasswordSubmitDisable = true;
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function () {
            if(typeof email !== 'undefined'){
                Connector.isEmailRegistered(email).then(
                    function (result) {
                        $scope.emailRegistered = result;
                        if(result){
                            $scope.forgotPasswordSubmitDisable = false;
                        }
                    },
                    function () {
                        console.error('Error while fetching isEmailRegistered');
                    }
                )

            }
        }, 1000);
    };
});