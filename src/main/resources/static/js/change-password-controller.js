'use strict';

app.controller('ChangePasswordCtrl', function ($scope, $rootScope, $uibModalInstance, Connector) {

    $scope.changePasswordWrapper = {};
    $scope.changePasswordSubmitDisable = false;

    $scope.ok = function () {
        $scope.changeEmailSubmitDisable = true;
        return Connector.changePassword($scope.changePasswordWrapper, createAuthorizationTokenHeader())
            .then(
                function (result) {
                    $scope.changePasswordSubmitDisable = false;
                    if(result.status !== 'ERROR'){
                        $rootScope.updateToken();
                    }
                    $uibModalInstance.close(result);
                    return result;
                },
                function (errResponse) {
                    $scope.changePasswordSubmitDisable = false;
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
});