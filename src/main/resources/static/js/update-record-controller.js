'use strict';

app.controller('UpdateRecordCtrl', function ($scope, $rootScope, $uibModalInstance, Connector, record, recordBefore) {
    console.log("UpdateRecordCtrl");

    $scope.record = record;
    $scope.recordWrapper = {};
    $scope.recordWrapper.recordId = null;
    $scope.recordWrapper.record = {};
    $scope.recordDate = new Date($scope.record.date);
    $scope.addRecordFormHolder = {};
    $scope.addRecordFormHolder.addEditRecordForm = {};
    $scope.addRecordSubmitDisable = false;

    $scope.ok = function (record, categoryName) {
        if (!angular.equals(record, recordBefore)) {
            $scope.recordWrapper.recordId = record.recordId;
            $scope.recordWrapper.record.amount = record.amount;
            $scope.recordWrapper.record.date = $scope.recordDate.getTime();
            if(record.comment != null){
                $scope.recordWrapper.record.comment = record.comment;
            }
            //noinspection JSUnresolvedVariable
            if (record.category.type == 'ADD_NEW') {
                var newCategory = $rootScope.addCategory({name: categoryName});
                newCategory.then(function(result){
                    if(result != null){
                        $scope.recordWrapper.record.categoryId = result.categoryId;
                        $scope.editRecord($scope.recordWrapper);
                    }
                }, function (errResponse) {
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.message);
                    console.error(JSON.stringify(errResponse));
                    $rootScope.authExceptionCheck(errResponse);
                });
            } else {
                //noinspection JSUnresolvedVariable
                $scope.recordWrapper.record.categoryId = record.category.categoryId;
                $scope.editRecord($scope.recordWrapper);
            }
        } else
            $uibModalInstance.close(record);
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.editRecord = function (record) {
        return Connector.editRecord(record, createAuthorizationTokenHeader())
            .then(
                function (record) {
                    $rootScope.updateRecordInRecordList(record);
                    $uibModalInstance.close(record);
                    return record;
                },
                function (errResponse) {
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.message);
                    console.error(JSON.stringify(errResponse));
                    $rootScope.authExceptionCheck(errResponse);
                    return null;
                }
            );
    };

    $scope.dateOptionsRecord = {
        formatYear: 'yy',
        maxDate: new Date(),
        startingDay: 1
    };

    $scope.setRecordDate = function (recordDate) {
        $scope.recordDate = $scope.checkDateInput(recordDate);
    };

    $scope.checkDateInput = function (date) {
        if (date > new Date()) {
            date = new Date();
        }
        return date;
    };

    $scope.openRecordDate = function () {
        $scope.popupRecordDate.opened = true;
    };

    $scope.popupRecordDate = {
        opened: false
    };
});