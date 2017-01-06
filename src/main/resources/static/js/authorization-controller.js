'use strict';

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

app.controller('AuthorizationController', function($scope, $rootScope, $location, Connector){
    console.log("AuthorizationController");
    $scope.isAutorized = false;
    $scope.formats = ['dd/MM/yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $rootScope.format = $scope.formats[0];
    $scope.signUpSubmitButtomDisabled = false;
    $scope.loginSubmitButtomDisabled = false;
    $scope.userDetails = {};
    $scope.credentials = {};
    $rootScope.mainWindowAlerts = [];
    $rootScope.authAddRecordBlockAlerts = [];

    $scope.login = function(credentials){
        $scope.loginSubmitButtomDisabled = true;
        return Connector.login(credentials)
            .then(
                function(result) {
                    console.log(result.token);
                    setToken(result.token);
                    $scope.isAutorized = true;
                    $location.path('/user-home');
                    $scope.loginSubmitButtomDisabled = false;
                },
                function(errResponse){
                    $scope.loginSubmitButtomDisabled = false;
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.data.message);
                    console.error('Error while fetching dataTable');
                }
            );
    };

    $scope.signup = function(userDetails){
        $scope.signUpSubmitButtomDisabled = true;
        return Connector.signup(userDetails)
            .then(
                function(result) {
                    console.log(result.token);
                    setToken(result.token);
                    $scope.isAutorized = true;
                    $location.path('/user-home');
                    $scope.signUpSubmitButtomDisabled = false;
                },
                function(errResponse){
                    $scope.signUpSubmitButtomDisabled = false;
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.data.message);
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

    $rootScope.addMainWindowAlert = function (type, msg) {
        $rootScope.mainWindowAlerts.push({type: type, msg: msg});
    };

    $rootScope.closeMainWindowAlert = function (index) {
        $rootScope.mainWindowAlerts.splice(index, 1);
    };

    $rootScope.addAuthAddRecordBlockAlert = function (type, msg) {
        $rootScope.authAddRecordBlockAlerts.push({type: type, msg: msg});
    };

    $rootScope.closeAuthAddRecordBlockAlert = function (index) {
        $rootScope.authAddRecordBlockAlerts.splice(index, 1);
    };


});