'use strict';

app.service('AlertsService', function($uibModal, $location, $rootScope, $state) {
    function getClassByStatus(status) {
        var cssClass;
        switch(status) {
            case 'ERROR':
                cssClass = 'text-error';
                break;
            case 'WARNING':
                cssClass = 'text-warning';
                break;
            case 'SUCCESS':
                cssClass = 'text-success';
                break;
            default:
                cssClass = 'text-info';
                break;
        }
        return cssClass;
    }

    this.setModelTitleClassByStatus = function (model) {
        model.titleClass = getClassByStatus(model.status);
        return model;
    };

    this.openAlertModal = function (templateName, model) {
        $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'pages/framework/modals/alerts/' + templateName,
            controller: 'AlertsCtrl',
            size: 'sm',
            resolve: {
                model: function () {
                    return model;
                }
            }
        });
    };

    this.createConfirmationPage = function (response) {
        $rootScope.confirmationPageData = response;
        $rootScope.confirmationPageData.class = getClassByStatus(response.status);
        $state.go('confirmation-page');
    };
});

app.controller('AlertsCtrl', function ($scope, $uibModalInstance, model) {
    $scope.model = model;
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.revive = function (credentials) {
        model.scope.reviveUser(credentials).then(function () {
            $uibModalInstance.dismiss('cancel');
        });
    };

});

app.controller('ConfirmationPageCtrl', function ($rootScope, $state) {
    $rootScope.showCarousel = false;
    if(typeof $rootScope.confirmationPageData === 'undefined'){
        $state.go('main');
    }
});