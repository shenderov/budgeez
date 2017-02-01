'use strict';

app.controller('RecordsController', function($scope, $rootScope, $http, Connector) {
    console.log("RecordsController");
    $scope.chartsCtrl = {};
    $scope.recordsListCtrl = {};
    $rootScope.purposesList = {};
    $scope.record = {};
    $scope.purposeName = null;
    $scope.recordDate = new Date().getTime();
    $scope.addRecordFormHolder = {};
    $scope.addRecordFormHolder.addEditRecordForm = {};
    $scope.addRecordSubmitDisable = false;
    $scope.controllerName = "RecordsController";

    $scope.getPurposesList = function(){
        Connector.getPurposesList(createAuthorizationTokenHeader())
            .then(
                function(purposesList) {
                    $rootScope.purposesList = purposesList;
                    $rootScope.purposesList.push({purposeId: 0, type:'ADD_NEW', name:'Add new...'});
                },
                function(errResponse){
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.data.message);
                    console.error(JSON.stringify(errResponse));
                }
            );
    };
    $scope.getPurposesList();

    $scope.addRecord = function(record, purposeName){
        record.date = $scope.recordDate;
        if(record.purpose.type == 'ADD_NEW'){
            var newPurpose = $scope.addPurpose({name: purposeName});
            newPurpose.then(function(result){
                if(result != null){
                    record.purpose = result.data;
                    $scope.addNewRecord(record);
                }
            }, function(error){
                $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.data.message);
                console.log("addRecord record error: " + JSON.stringify(error));
            });
        }else {
            $scope.addNewRecord(record);
        }
    };

    $scope.addNewRecord = function(record){
        $scope.addRecordSubmitDisable = true;
        return Connector.addRecord(record, createAuthorizationTokenHeader())
            .then(
                function(record) {
                    $scope.record = {};
                    $rootScope.addAuthAddRecordBlockAlert("success", "Record is added");
                    $scope.$broadcast('defaultAddRecordAction', {
                        record: record
                    });
                    $scope.addRecordSubmitDisable = false;
                    $scope.addRecordFormHolder.addEditRecordForm.$setUntouched();
                    $scope.addRecordFormHolder.addEditRecordForm.$setPristine();
                    return record;
                },
                function(errResponse){
                    console.log("Add new record error: " + JSON.stringify(errResponse));
                    //console.error('Error while fetching dataTable');
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.data.message);
                   // $scope.addAddEditFormAlert("danger", "Can not add record");
                    $scope.addRecordSubmitDisable = false;
                    return errResponse;
                }
            );
    };

    $scope.addPurpose = function(purpose){
        return Connector.addCustomPurpose(purpose, createAuthorizationTokenHeader())
            .then(
                function(purpose) {
                    //$scope.purpose = purpose;
                    console.log("addPurpose done: " + JSON.stringify(purpose));
                    return purpose;
                },
                function(errResponse){
                    console.error("addPurpose error: " + JSON.stringify(errResponse));
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.data.message);
                }
            );
    };

    $scope.dateOptionsRecord = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.setRecordDate = function(recordDate){
        $scope.recordDate = recordDate;
    };

    $scope.openRecordDate = function() {
        $scope.popupRecordDate.opened = true;
    };

    $scope.popupRecordDate = {
        opened: false
    };
});