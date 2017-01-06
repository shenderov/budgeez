'use strict';

app.controller('RecordListController', function ($scope, $rootScope, $http, Connector, $q, $uibModal) {
    console.log("RecordListController");
    var date = new Date();
    $scope.startDate = new Date(date.getFullYear(), date.getMonth(), 1);
    $scope.endDate = new Date(date.getFullYear(), date.getMonth() + 1, 0, 23, 59, 59);
    $scope.showLimitWell = {};
    $scope.recordsList = {};
    //$scope.recordListAlerts = [];

    $scope.getRecordsList = function (startDate, endDate) {
        $scope.datePicker = {};
        if ((startDate > 0) && (endDate > 0)) {
            $scope.datePicker.startDate = startDate;
            $scope.datePicker.endDate = endDate;
        }
        return Connector.getRecordsList($scope.datePicker, createAuthorizationTokenHeader())
            .then(
                function (recordsList) {
                    $scope.recordsList = recordsList.content;
                    if (!recordsList.last) {
                        //noinspection JSUnresolvedVariable
                        $scope.showLimitWell.message = recordsList.totalElements + " records are found. Only first " + recordsList.numberOfElements + " are shown. Please, specify your request";
                        $scope.showLimitWell.show = true;
                    } else {
                        $scope.showLimitWell.show = false;
                    }
                    //noinspection JSUnresolvedVariable
                    $scope.showTable = recordsList.totalElements > 0;
                },
                function (errResponse) {
                    $rootScope.addMainWindowAlert("danger", errResponse.data.message);
                    console.error(JSON.stringify(errResponse));
                }
            );
    };
    $scope.getRecordsList(0, 0);

    $rootScope.addRecordToRecordList = function (record) {
        $scope.recordsList.push(record);
    };

    $rootScope.deleteRecordFromRecordList = function ($index) {
        $scope.recordsList.splice($index, 1);
    };

    $rootScope.updateRecordInRecordList = function (record) {
        for (var i = 0; i < $scope.recordsList.length; i++) {
            //noinspection JSUnresolvedVariable
            if ($scope.recordsList[i].recordId === record.recordId) {
                $scope.recordsList[i] = record;
                break;
            }
        }
    };

    $scope.deleteRecord = function (recordId, $index) {
        return Connector.deleteRecord(recordId, createAuthorizationTokenHeader())
            .then(
                function (result) {
                    if (result) {
                        $rootScope.deleteRecordFromRecordList($index);
                        $rootScope.addMainWindowAlert("success", "Record Deleted");
                    }
                },
                function (errResponse) {
                    //$scope.addRecordListAlert("danger", "Error Delete Record");
                    $rootScope.addMainWindowAlert("danger", errResponse.data.message);
                    console.error('Error while fetching dataTable');
                    console.error(JSON.stringify(errResponse));
                }
            );
    };

    $scope.openUpdateModal = function (recordBefore, index) {
        $scope.recordBefore = recordBefore;
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'pages/framework/modals/records/edit-record-modal.html',
            controller: 'UpdateRecordCtrl',
            size: 'sm',
            resolve: {
                record: function () {
                    return angular.copy(recordBefore);
                },
                recordBefore: function () {
                    return recordBefore;
                }
            }
        });

        modalInstance.result.then(function (record) {
            if ((!angular.equals($scope.recordBefore, record)) && (record != null)) {
                $scope.recordsList[index] = record;
                $rootScope.addMainWindowAlert('success', 'Record Updated');
            } else if (record == null) {
                $rootScope.addMainWindowAlert('danger', 'Record can not be updated');
            }
        }, function () {

        });
    };

    $scope.dateOptionsStart = {
        formatYear: 'yy',
        minDate: new Date(2000, 1, 1),
        startingDay: 1
    };

    $scope.dateOptionsEnd = {
        formatYear: 'yy',
        minDate: $scope.startDate,
        startingDay: 1
    };

    $scope.setStartDate = function (startDate) {
        $scope.startDate = $scope.checkDateInput(startDate);
        $scope.setMaxDate();
        $scope.dateOptionsEnd.minDate = $scope.startDate;
    };

    $scope.setEndDate = function (endDate) {
        $scope.endDate = $scope.checkDateInput(new Date(endDate.getFullYear(), endDate.getMonth(), endDate.getDay(), 23, 59, 59));
        $scope.setMaxDate();
        $scope.dateOptionsStart.maxDate = $scope.endDate;
    };

    $scope.setMaxDate = function () {
        if ($scope.startDate > $scope.endDate) {
            $scope.endDate = $scope.startDate;
            $scope.dateOptionsStart.maxDate = $scope.endDate;
            $scope.dateOptionsEnd.maxDate = $scope.endDate;
        }
    };

    $scope.checkDateInput = function (date) {
        if (date > new Date()) {
            date = new Date();
        }
        return date;
    };

    $scope.openStart = function () {
        $scope.popupStart.opened = true;
    };

    $scope.openEnd = function () {
        $scope.popupEnd.opened = true;
    };

    $scope.popupStart = {
        opened: false
    };

    $scope.popupEnd = {
        opened: false
    };
});