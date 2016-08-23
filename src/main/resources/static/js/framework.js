'use strict';

var app = angular.module('KamaBizbazti', ['ngRoute','ngAnimate', 'ui.bootstrap']);

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
}])

app.controller('General', function($scope, $location){
    console.log("General");
    //console.log("General - Load cookies");
    //console.log("General - Check token");
    //console.log("General - Get default / Personalized settings");
    //console.log("General - Get default / Personalized localization");
    //console.log("General - Get template tranlation");
    $scope.isAutorized = false;
    $scope.formats = ['dd/MM/yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];

    //Kostya
    $scope.loginKostyl = function() {
        $scope.isAutorized = true;
        $location.path('/user-home');
    }

    $scope.logoutKostyl = function() {
        $scope.isAutorized = false;
        $location.path('/');
    }

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

    $scope.getDataTable = function(chartSelection){
        return Connector.getGeneralDataTable(chartSelection)
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
        return Connector.getUserDefaultDataTable()
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

    $scope.getDataTable = function(chartSelection){
        return Connector.getUserDataTable(chartSelection)
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





app.controller('RecordsController', function($scope, $http, Connector) {
    console.log("RecordsController");

    $scope.purpose = null;
    $scope.record = null;

    $scope.getPurposesList = function(){
        Connector.getPurposesList()
            .then(
                function(purposesList) {
                    console.log(JSON.stringify(purposesList));
                    $scope.purposesList = purposesList.data;
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            );
    };
    $scope.getPurposesList();

    $scope.addRecord = function(recordName, recordAmount, recordPurpose, recordDate, newwPurposeName){
        //if(purpose == null){
        //    console.log("Purpose is null");
        //}
        //if($scope.purposesList.indexOf(purpose)){
        //    console.log("Purpose list contains purpose");
        //}
        if((recordName) && (recordAmount != null) && (recordPurpose != null) && (recordDate != null)){
            //console.log("record " + JSON.stringify(record));
            if(newwPurposeName != null){
                $scope.addPurpose(newwPurposeName).then(
                    function(createdPurpose){
                        recordPurpose = createdPurpose;
                        Connector.addRecord(record)

                    }

                )
            }else {

            }
               // console.log("purpose " + purpose);
        }else{
            alert("record is null");
        }


        //if(purpose != null && $scope.purposesList.contains(purpose)){
        //    var newPurpose = $scope.addPurpose(purpose);
        //}



        //Connector.getPurposesList()
        //    .then(
        //        function(purposesList) {
        //            console.log(JSON.stringify(purposesList));
        //            $scope.purposesList = purposesList;
        //        },
        //        function(errResponse){
        //            console.error('Error while fetching dataTable');
        //        }
        //    );
    };



    $scope.addPurpose = function(purpose){
        return Connector.addCustomPurpose(purpose)
            .then(
                function(purpose) {
                    console.log(JSON.stringify(purposesList));
                    //$scope.purpose = purpose;
                    return purpose;
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            );
    };


});





app.controller('LoginController', function($scope, $http, Connector, $location) {
    console.log("LoginController");
    $scope.login = function(credentials){
        console.log(JSON.stringify("isAutorized " + $scope.isAutorized));
        console.log(JSON.stringify(credentials));
        return Connector.login(credentials)
            .then(
                function(result) {
                    console.log(JSON.stringify(result));
                    //$scope.purpose = purpose;
                   // $scope.isAutorized = true;
                   // $location.path('/user-home');
                    console.log(JSON.stringify($scope.isAutorized));
                    $scope.loginKostyl();
                    return result;
                },
                function(errResponse){
                    console.error('Error while fetching dataTable');
                }
            );
    };




});









