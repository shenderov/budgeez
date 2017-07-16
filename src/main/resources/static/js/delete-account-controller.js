'use strict';

app.controller('DeleteAccountCtrl', function ($scope, $rootScope, $uibModalInstance, Connector) {

    $scope.deleteAccountWrapper = {};
    $scope.deleteAccountSubmitDisable = false;

    $scope.ok = function () {
        $scope.deleteAccountSubmitDisable = true;
        return Connector.deleteAccount($scope.deleteAccountWrapper, createAuthorizationTokenHeader())
            .then(
                function (result) {
                    $scope.deleteAccountSubmitDisable = false;
                    $uibModalInstance.close(result);
                    return result;
                },
                function (errResponse) {
                    $scope.deleteAccountSubmitDisable = false;
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