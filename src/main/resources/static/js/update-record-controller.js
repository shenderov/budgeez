'use strict';

app.controller('UpdateRecordCtrl', function ($scope, $rootScope, $uibModalInstance, Connector, record, recordBefore) {
    console.log("UpdateRecordCtrl");

    $scope.record = record;
    $scope.recordDate = new Date($scope.record.date);
    $scope.addRecordFormHolder = {};
    $scope.addRecordFormHolder.addEditRecordForm = {};
    $scope.addRecordSubmitDisable = false;

    $scope.ok = function (record, purposeName) {
        if (!angular.equals(record, recordBefore)) {
            //noinspection JSUnresolvedFunction
            record.date = $scope.recordDate.getTime();
            var updatedRecord;
            if (record.purpose.type == 'ADD_NEW') {
                var newPurpose = $scope.addPurpose({name: purposeName});
                newPurpose.then(function (result) {
                    if (result != null) {
                        record.purpose = result;
                        updatedRecord = $scope.editRecord(record);
                    } else
                        updatedRecord = null;
                }, function (errResponse) {
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.data.message);
                    console.error(JSON.stringify(errResponse));
                });
            } else {
                updatedRecord = $scope.editRecord(record);
            }
            updatedRecord.then(function (record) {
                $uibModalInstance.close(record);
            });
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
                    return record;
                },
                function (errResponse) {
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.data.message);
                    console.error(JSON.stringify(errResponse));
                    return null;
                }
            );
    };

    $scope.addPurpose = function (purpose) {
        return Connector.addCustomPurpose(purpose, createAuthorizationTokenHeader())
            .then(
                function (purpose) {
                    return purpose;
                },
                function (errResponse) {
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.data.message);
                    console.error(JSON.stringify(errResponse));
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