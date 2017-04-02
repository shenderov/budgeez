'use strict';

app.controller('RecordsController', function($scope, $rootScope, $http, Connector) {
    console.log("RecordsController");
    $scope.chartsCtrl = {};
    $scope.recordsListCtrl = {};
    $rootScope.categoriesList = {};
    $scope.record = {};
    $scope.recordWrapper = {};
    $scope.categoryName = null;
    $scope.addCategoryElement = {categoryId: 0, type:'ADD_NEW', name:'Add new...'};
    $scope.recordDate = new Date().getTime();
    $scope.addRecordFormHolder = {};
    $scope.addRecordFormHolder.addEditRecordForm = {};
    $scope.addRecordSubmitDisable = false;
    $scope.controllerName = "RecordsController";

    $scope.getCategoriesList = function(){
        Connector.getCategoriesList(createAuthorizationTokenHeader())
            .then(
                function(categoriesList) {
                    $rootScope.categoriesList = categoriesList;
                    $rootScope.categoriesList.push($scope.addCategoryElement);
                },
                function(errResponse){
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.message);
                    console.error(JSON.stringify(errResponse));
                    $rootScope.authExceptionCheck(errResponse);
                }
            );
    };
    $scope.$on('authorized', function () {
        $scope.getCategoriesList();
    });

    $scope.addRecord = function(record, categoryName){
        $scope.recordWrapper.date = $scope.recordDate;
        $scope.recordWrapper.amount = record.amount;
        if(record.comment != null){
            $scope.recordWrapper.comment = record.comment;
        }
        //noinspection JSUnresolvedVariable
        if(record.category.type == 'ADD_NEW'){
            var newCategory = $rootScope.addCategory({name: categoryName});
            newCategory.then(function(result){
                if(result != null){
                    $scope.recordWrapper.categoryId = result.categoryId;
                    $scope.addNewRecord($scope.recordWrapper);
                }
            }, function(error){
                $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.message);
                console.log("addRecord record error: " + JSON.stringify(error));
            });
        }else {
            //noinspection JSUnresolvedVariable
            $scope.recordWrapper.categoryId = record.category.categoryId;
            $scope.addNewRecord($scope.recordWrapper);
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
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.message);
                    $scope.addRecordSubmitDisable = false;
                    $rootScope.authExceptionCheck(errResponse);
                    return errResponse;
                }
            );
    };

    $rootScope.addCategory = function(category){
        return Connector.addCustomCategory(category, createAuthorizationTokenHeader())
            .then(
                function(category) {
                    $rootScope.categoriesList.pop();
                    $rootScope.categoriesList.push(category, $scope.addCategoryElement);
                    return category;
                },
                function(errResponse){
                    console.error("addCategory error: " + JSON.stringify(errResponse));
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.message);
                    $rootScope.authExceptionCheck(errResponse);
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