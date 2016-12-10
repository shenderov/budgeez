'use strict';

var app = angular.module('KamaBizbazti', ['ngRoute','ngAnimate', 'ui.bootstrap', 'dialogs.main',
    'angularjs-dropdown-multiselect', 'rzModule']);

var TOKEN_KEY = "token";

function setToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function removeToken() {
    localStorage.removeItem(TOKEN_KEY);
}

function createAuthorizationTokenHeader() {
    var token = getToken();
    if (token) {
        return {"Authorization": token};
    } else {
        return {};
    }
}

app.config(function($routeProvider) {
    console.log("Router");
    $routeProvider
        .when('/', {
            templateUrl : 'pages/main/main-tab.html',
            controller  : 'MainController'
        })
        .when('/user-home', {
            templateUrl : 'pages/user-home/user-home-tab.html',
            controller  : 'UserHomeController'
        })
        .when('/settings', {
            templateUrl : 'pages/settings/settings-tab.html',
            controller  : 'SettingsController'
        })
        .otherwise({redirectTo: '/'});
});

app.config(['$httpProvider', function($httpProvider){
    $httpProvider.defaults.withCredentials = true;
}]);



app.controller('General', function($scope, $rootScope, $location, Connector, $uibModal){
    console.log("General");
    //console.log("General - Load cookies");
    //console.log("General - Check token");
    //console.log("General - Get default / Personalized settings");
    //console.log("General - Get default / Personalized localization");
    //console.log("General - Get template tranlation");
    $scope.isAutorized = false;
    $scope.formats = ['dd/MM/yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $rootScope.format = $scope.formats[0];

    $scope.login = function(credentials){
        return Connector.login(credentials)
            .then(
                function(result) {
                    console.log(result.token);
                    setToken(result.token);
                    $scope.isAutorized = true;
                    $location.path('/user-home');
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            );
    };

    $scope.signup = function(userDetails){
        return Connector.signup(userDetails)
            .then(
                function(result) {
                    console.log(result.token);
                    setToken(result.token);
                    $scope.isAutorized = true;
                    $location.path('/user-home');
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            );
    };

    $scope.logout = function(){
        $scope.isAutorized = false;
        $location.path('/');
        removeToken();
    };

    $scope.loginKostyl = function() {
        $scope.isAutorized = true;
        $location.path('/user-home');
    };

    $scope.logoutKostyl = function() {
        $scope.isAutorized = false;
        $location.path('/');
        removeToken();
    };

});

app.controller('MainController', function($scope, $http, $templateCache, Connector, $q, $rootScope) {
    console.log("Main");
	//$templateCache.remove('pages/framework/statistics/charts-view-module.html');

    $scope.activeChartSelection = null;
    $scope.chartWrapper = null;
    $scope.chartSelectionsList = null;

    $scope.getGeneralSelectionsList = function(){
        Connector.getGeneralSelectionsList()
            .then(
                function(chartSelectionsList) {
                    $scope.chartSelectionsList = chartSelectionsList;
                    $scope.activeChartSelection = $scope.chartSelectionsList[0];
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            );
    };
    $scope.getGeneralSelectionsList();

    $scope.getDefaultDataTable = function(){
        return Connector.getGeneralDefaultDataTable()
            .then(
                function(defaultChartWrapper) {
                    return defaultChartWrapper;
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            )
    };

    $scope.getDataTable = function(chartRequestWrapper){
        return Connector.getGeneralDataTable(chartRequestWrapper)
            .then(
                function(chartWrapper) {
                    //$scope.chartWrapper = chartWrapper;
                    return chartWrapper;
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            )
    };
});

app.controller('UserHomeController', function($scope, $http, $templateCache, Connector, $q, $rootScope) {
    console.log("User");
	//$templateCache.remove('pages/framework/statistics/charts-view-module.html');
    $scope.activeChartSelection = null;
    $scope.chartWrapper = null;
    $scope.chartSelectionsList = null;

    $scope.getUserSelectionsList = function(){
        Connector.getUserSelectionsList()
            .then(
                function(chartSelectionsList) {
                    $scope.chartSelectionsList = chartSelectionsList;
                    $scope.activeChartSelection = $scope.chartSelectionsList[0];
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            );
    };
    $scope.getUserSelectionsList();

    $scope.getDefaultDataTable = function(){
        return Connector.getUserDefaultDataTable(createAuthorizationTokenHeader())
            .then(
                function(defaultChartWrapper) {
                   // $scope.defaultChartWrapper = defaultChartWrapper;
                    return defaultChartWrapper;
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            );
    };

    $scope.getDataTable = function(chartRequestWrapper){
        return Connector.getUserDataTable(chartRequestWrapper, createAuthorizationTokenHeader())
            .then(
                function(chartWrapper) {
                    return chartWrapper;
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            )
    };
});

app.controller('SettingsController', function($scope, $http) {
    console.log("SettingsController");
});


app.controller('RecordsController', function($scope, $rootScope, $http, Connector, $q, $uibModal) {
    console.log("RecordsController");
    //$scope.purpose = null;
    //$scope.record = {};
    $scope.recordDate = new Date().getTime();

    $scope.addEditFormAlerts = [];

    $scope.addAddEditFormAlert = function(type, msg) {
        $scope.addEditFormAlerts.push({ type: type, msg: msg });
    };

    $scope.closeAddEditFormAlert = function(index) {
        $scope.addEditFormAlerts.splice(index, 1);
    };

    $scope.getPurposesList = function(){
        Connector.getPurposesList(createAuthorizationTokenHeader())
            .then(
                function(purposesList) {
                    $rootScope.purposesList = purposesList;
                    $rootScope.purposesList.push({purposeId: 0, type:'ADD_NEW', name:'Add new...'});
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            );
    };
    $scope.getPurposesList();

    $scope.addRecord = function(record, purposeName){
        console.log("Scope: " + JSON.stringify($scope.recordsList));
        record.date = $scope.recordDate;
        if(record.purpose.type == 'ADD_NEW'){
            var newPurpose = $scope.addPurpose({name: purposeName});
            newPurpose.then(function(result){
                console.log("addRecord status: " + JSON.stringify(result));
                if(result != null){
                    record.purpose = result.data;
                    console.log("Add new purpose: " + JSON.stringify(result));
                    $scope.addNewRecord(record);
                }
            }, function(error){
                console.log("addRecord record error: " + JSON.stringify(error));
            });
        }else {
            $scope.addNewRecord(record);
        }
    };

    $scope.addNewRecord = function(record){
        return Connector.addRecord(record, createAuthorizationTokenHeader())
            .then(
                function(record) {
                    $scope.record = {};
                    $scope.addAddEditFormAlert("success", "Record is added");
                    $rootScope.addRecordToRecordList(record);
                    $rootScope.getAndRedrawChart();
                    return record;
                },
                function(errResponse){
                    console.log("Add new record error: " + JSON.stringify(errResponse));
                    //console.error('Error while fetching dataTable');
                    $scope.addAddEditFormAlert("danger", "Can not add record");
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
                    //console.error('Error while fetching dataTable');
                }
            );
    };


    $scope.dateOptionsRecord = {
        formatYear: 'yy',
        maxDate: new Date(),
        startingDay: 1
    };

    $scope.setRecordDate = function(recordDate){
        $scope.recordDate = $scope.checkDateInput(recordDate);
    };

    $scope.checkDateInput = function(date){
        if(date > new Date()){
            date = new Date();
        }
        return date;
    };

    $scope.openRecordDate = function() {
        $scope.popupRecordDate.opened = true;
    };

    $scope.popupRecordDate = {
        opened: false
    };

});

app.controller('RecordListController', function($scope, $rootScope, $http, Connector, $q, $uibModal, $timeout, $filter) {
    console.log("RecordListController");
    //$scope.visible = true;
    var date = new Date();
    $scope.startDate = new Date(date.getFullYear(), date.getMonth(), 1);
    $scope.endDate = new Date(date.getFullYear(), date.getMonth() + 1, 0, 23, 59, 59);
    $scope.showLimitWell = {};
        $scope.getRecordsList = function(startDate, endDate){
        $scope.datePicker = {};
        if((startDate > 0) && (endDate > 0)){
            $scope.datePicker.startDate = startDate;
            $scope.datePicker.endDate = endDate;
        }
        return Connector.getRecordsList($scope.datePicker, createAuthorizationTokenHeader())
            .then(
                function(recordsList) {
                    $scope.recordsList = recordsList.content;
                    if(!recordsList.last){
                        $scope.showLimitWell.message = recordsList.totalElements + " records are found. Only first " + recordsList.numberOfElements + " are shown. Please, specify your request";
                        $scope.showLimitWell.show = true;
                        //$scope.addAlert("warning", $scope.showLimitWell.message);
                    }else{
                        $scope.showLimitWell.show = false;
                    }
                    if(recordsList.totalElements > 0)
                        $scope.showTable = true;
                    else
                        $scope.showTable = false;

                    // $scope.actualPurposesList = _.uniq(recordsList, function (e) {
                    //     return e.purpose.purposeId;
                    // });
                    // $scope.actualPurposesList  = _.pluck($scope.actualPurposesList, 'purpose');
                    // $scope.selectedPurposes = angular.copy($scope.actualPurposesList);
                    // $scope.minAmount = Math.ceil((_.min(recordsList, 'amount').amount)*100)/100;
                    // $scope.maxAmount = Math.ceil((_.max(recordsList, 'amount').amount)*100)/100;
                    // $scope.rangeSlider = {
                    //     minValue: $scope.minAmount,
                    //     maxValue: $scope.maxAmount,
                    //     options: {
                    //         floor: $scope.minAmount,
                    //         ceil: $scope.maxAmount,
                    //         step: 0.01,
                    //         precision: 2,
                    //         noSwitching: true
                    //     }
                    // };
                    //return recordsList;
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            );
    };
    $scope.getRecordsList(0, 0);

    // $scope.filterInRange = function(prop, minVal, maxVal){
    //     return function(item){
    //         if ((item[prop] > minVal) && (item[prop] < maxVal)) return true;
    //     }
    // };
    //
    // $scope.refreshSlider = function () {
    //     console.log("!!!!!!!!!!");
    //     console.log(JSON.stringify($scope.actualPurposesList));
    //     $scope.selectedPurposes = angular.copy($scope.actualPurposesList);
    //     $scope.visible = true;
    //     $timeout(function () {
    //         $scope.$broadcast('rzSliderForceRender');
    //     });
    // };
    //
    // $scope.purposeMultiselectSettings = {
    //     displayProp: 'name',
    //     idProp: 'purposeId',
    //     externalIdProp: '',
    //     scrollable: true,
    //     scrollableHeight: '300px',
    //     dynamicTitle: false,
    // };
    // $scope.purposeMultiselectButtonText = {buttonDefaultText: 'Select Purposes'};

    //Range slider config

    $scope.recordListAlerts = [];

    $scope.addRecordListAlert = function(type, msg) {
        $scope.recordListAlerts.push({ type: type, msg: msg });
    };

    $scope.closeRecordListAlert = function(index) {
        $scope.recordListAlerts.splice(index, 1);
    };

    $rootScope.addRecordToRecordList = function (record) {
        $scope.recordsList.push(record);
    };

    $rootScope.deleteRecordFromRecordList = function ($index) {
        $scope.recordsList.splice($index, 1);
    };

    $rootScope.updateRecordInRecordList = function (record) {
        for(var i = 0; i < $scope.recordsList.length; i++){
            if($scope.recordsList[i].recordId === record.recordId){
                $scope.recordsList[i] = record;
                break;
            }
        }
        };

    $scope.deleteRecord = function(recordId, $index){
        return Connector.deleteRecord(recordId, createAuthorizationTokenHeader())
            .then(
                function(result) {
                    if(result){
                        //$scope.recordsList.splice($index, 1);
                        $rootScope.deleteRecordFromRecordList($index);
                        $scope.addRecordListAlert("success", "Record Deleted");
                    }
                },
                function(errResponse){
                    $scope.addRecordListAlert("danger", "Error Delete Record");
                    console.error('Error while fetching dataTable');
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
                // purposesList: function () {
                //     return $rootScope.purposesList;
                // }
            }
        });

        modalInstance.result.then(function (record) {
            if((!angular.equals($scope.recordBefore, record)) && (record != null)){
                $scope.recordsList[index] = record;
                $scope.addRecordListAlert('success', 'Record Updated');
            }else if(record == null){
                $scope.addRecordListAlert('danger', 'Record can not be updated');
            }
        }, function () {

        });
    };

    $scope.dateOptionsStart = {
        formatYear: 'yy',
        minDate: new Date(2000, 1, 1),
        //maxDate: new Date(),
        startingDay: 1
    };

    $scope.dateOptionsEnd = {
        formatYear: 'yy',
        minDate: $scope.startDate,
        //maxDate: new Date(),
        startingDay: 1
    };

    $scope.setStartDate = function(startDate){
        $scope.startDate = $scope.checkDateInput(startDate);
        $scope.setMaxDate();
        $scope.dateOptionsEnd.minDate = $scope.startDate;
    };

    $scope.setEndDate = function(endDate){
        $scope.endDate = $scope.checkDateInput(new Date(endDate.getFullYear(), endDate.getMonth() , endDate.getDay(), 23, 59, 59));
        $scope.setMaxDate();
        $scope.dateOptionsStart.maxDate = $scope.endDate;
    };

    $scope.setMaxDate = function(){
        if($scope.startDate > $scope.endDate){
            $scope.endDate = $scope.startDate;
            $scope.dateOptionsStart.maxDate = $scope.endDate;
            $scope.dateOptionsEnd.maxDate = $scope.endDate;
        }
    };

    $scope.checkDateInput = function(date){
        if(date > new Date()){
            date = new Date();
        }
        return date;
    };

    $scope.openStart = function() {
        $scope.popupStart.opened = true;
    };

    $scope.openEnd = function() {
        $scope.popupEnd.opened = true;
    };

    $scope.popupStart = {
        opened: false
    };

    $scope.popupEnd = {
        opened: false
    };
});

// app.filter('actual', function ($filter) {
//     return function (list, arrayFilter, element) {
//         if(arrayFilter){
//             return $filter("filter")(list, function (listItem) {
//                 return arrayFilter.indexOf(listItem[element]) != -1;
//             })
//         }
//     }
// });

app.controller('UpdateRecordCtrl', function($scope, $rootScope, $uibModalInstance, Connector, record, recordBefore) {
    console.log("UpdateRecordCtrl");

    $scope.addEditFormAlerts = [];

    $scope.addAddEditFormAlert = function(type, msg) {
        $scope.addEditFormAlerts.push({ type: type, msg: msg });
    };

    $scope.closeAddEditFormAlert = function(index) {
        $scope.addEditFormAlerts.splice(index, 1);
    };

    $scope.record = record;
    $scope.recordDate = new Date($scope.record.date);
    $scope.dateOptionsRecord = {
        formatYear: 'yy',
        maxDate: new Date(),
        startingDay: 1
    };

    $scope.setRecordDate = function(recordDate){
        $scope.recordDate = $scope.checkDateInput(recordDate);
    };

    $scope.checkDateInput = function(date){
        if(date > new Date()){
            date = new Date();
        }
        return date;
    };

    $scope.openRecordDate = function() {
        $scope.popupRecordDate.opened = true;
    };

    $scope.popupRecordDate = {
        opened: false
    };

    $scope.ok = function (record, purposeName) {
        if(!angular.equals(record, recordBefore)){
            record.date = $scope.recordDate.getTime();
            var updatedRecord;
            if(record.purpose.type == 'ADD_NEW'){
                var newPurpose = $scope.addPurpose({name: purposeName});
                newPurpose.then(function(result){
                    if(result != null){
                        record.purpose = result;
                        updatedRecord = $scope.editRecord(record);
                    }else
                        updatedRecord = null;
                }, function(){
                    console.log("Error");
                });
            }else {
                updatedRecord = $scope.editRecord(record);
            }
            updatedRecord.then(function (record) {
                $uibModalInstance.close(record);
            });
        }else
            $uibModalInstance.close(record);
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.editRecord = function(record){
        return Connector.editRecord(record, createAuthorizationTokenHeader())
            .then(
                function(record) {
                    console.log("updated: " + JSON.stringify(record));
                    $rootScope.updateRecordInRecordList(record);
                    return record;
                    //$scope.record = {};
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                    return null;
                }
            );
    };

    $scope.addPurpose = function(purpose){
        return Connector.addCustomPurpose(purpose, createAuthorizationTokenHeader())
            .then(
                function(purpose) {
                    //$scope.purpose = purpose;
                    return purpose;
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                    return null;
                }
            );
    };
});








