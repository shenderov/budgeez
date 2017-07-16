'use strict';

app.controller('SettingsController', function($scope, $rootScope, $http, $uibModal, AlertsService, Connector) {

    $rootScope.authCheck();
    $rootScope.showCarousel = false;
    $scope.userDetailsForm = {};
    $scope.languages = {};
    $scope.currencies = {};
    $scope.accountDetails = {};
    $scope.accountDetailsCopy = {};
    $scope.status = {};
    $scope.updateAccountDetailsSubmitButtonDisabled = false;

    function getStatus(userStatus) {
        var status = {};
        switch(userStatus) {
            case 'PENDING_ACTIVATION':
                status.statusClass = 'text-warning';
                status.statusGlyphicon = 'glyphicon-hourglass';
                status.statusName = 'Pending Activation';
                break;
            case 'ACTIVE':
                status.statusClass = 'text-success';
                status.statusGlyphicon = 'glyphicon-off';
                status.statusName = 'Active';
                break;
            case 'FAILED_ACTIVATION':
                status.statusClass = 'text-danger';
                status.statusGlyphicon = 'glyphicon-alert';
                status.statusName = 'Failed activation';
                break;
        }
        return status;
    }

    $scope.getLanguages = function () {
        Connector.getLanguages()
            .then(
                function (languages) {
                    $scope.languages = languages;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addSettingsAlert('danger', errResponse.message);
                }
            );
    };
    $scope.getLanguages();

    $scope.getCurrencies= function () {
        Connector.getCurrencies()
            .then(
                function (currencies) {
                    $scope.currencies = currencies;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addSettingsAlert('danger', errResponse.message);
                }
            );
    };
    $scope.getCurrencies();

    $scope.getUserDetails = function () {
        Connector.getUserDetails(createAuthorizationTokenHeader())
            .then(
                function (userDetails) {
                    $scope.accountDetails = userDetails;
                    angular.copy($scope.accountDetails, $scope.accountDetailsCopy);
                    $scope.status = getStatus(userDetails.status);
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addSettingsAlert('danger', errResponse.message);
                    $rootScope.authExceptionCheck(errResponse);
                }
            );
    };
    if($scope.isAutorized)
        $scope.getUserDetails();

    $scope.updateUserDetails = function () {
        if (!angular.equals($scope.accountDetails, $scope.accountDetailsCopy)) {
            $scope.updateAccountDetailsSubmitButtonDisabled = true;
            Connector.updateUserDetails($scope.accountDetails, createAuthorizationTokenHeader())
                .then(
                    function (userDetails) {
                        $scope.accountDetails = userDetails;
                        angular.copy($scope.accountDetails, $scope.accountDetailsCopy);
                        if(userDetails.activated){
                            $scope.statusClass = "text-success";
                            $scope.statusGlyphicon = "glyphicon-off";
                            $scope.status = "Active"
                        }else{
                            $scope.statusClass = "text-warning";
                            $scope.statusGlyphicon = "glyphicon-alert";
                            $scope.status = "Pending Activation";
                        }
                        $scope.updateAccountDetailsSubmitButtonDisabled = false;
                        $rootScope.addSettingsAlert('success', "User details updated successfully");
                    },
                    function (errResponse) {
                        console.error(JSON.stringify(errResponse));
                        $scope.updateAccountDetailsSubmitButtonDisabled = false;
                        $rootScope.addSettingsAlert('danger', errResponse.message);
                        $rootScope.authExceptionCheck(errResponse);
                    }
                );
        }
    };

    $scope.openChangeEmailModal = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'pages/framework/modals/settings/change-email-modal.html',
            controller: 'ChangeEmailCtrl',
            size: 'sm',
            resolve: {
                currentEmail: function () {
                    return $scope.accountDetails.email;
                }
            }
        });
        modalInstance.result.then(function (result) {
            AlertsService.openAlertModal('alert-general.html', AlertsService.setModelTitleClassByStatus(result));
        }, function (result) {
            console.log(JSON.stringify("error: " + result));
        });
    };

    $scope.openChangePasswordModal = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'pages/framework/modals/settings/change-password-modal.html',
            controller: 'ChangePasswordCtrl',
            size: 'sm'
        });
        modalInstance.result.then(function (result) {
            AlertsService.openAlertModal('alert-general.html', AlertsService.setModelTitleClassByStatus(result));
        }, function (result) {
            console.log(JSON.stringify("error: " + result));
        });
    };

    $scope.openDeleteAccountModal = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'pages/framework/modals/settings/delete-account-modal.html',
            controller: 'DeleteAccountCtrl',
            size: 'sm'
        });
        modalInstance.result.then(function (result) {
            AlertsService.openAlertModal('alert-general.html', AlertsService.setModelTitleClassByStatus(result));
            if(result.status === 'SUCCESS'){
                $scope.logout();
            }
        }, function (result) {
            console.log(JSON.stringify("error: " + result));
        });
    };
});

app.filter('range', function () {
    return function (input, start, end) {
        var direction;
        start = parseInt(start);
        end = parseInt(end);
        if (start === end) { return [start]; }
        direction = (start <= end) ? 1 : -1;
        while (start != end) {
            input.push(start);
            if (direction < 0 && start === end + 1) {
                input.push(end);
            }
            if (direction > 0 && start === end - 1) {
                input.push(end);
            }
            start += direction;
        }
        return input;
    };
});