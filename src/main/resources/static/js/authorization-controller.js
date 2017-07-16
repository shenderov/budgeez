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

app.controller('AuthorizationController', function($scope, $rootScope, $location, Connector, AlertsService){
    $scope.isAutorized = false;
    $scope.formats = ['dd/MM/yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $rootScope.format = $scope.formats[0];
    $scope.buildInfo = {};
    $scope.signUpSubmitButtomDisabled = false;
    $scope.loginSubmitButtomDisabled = false;
    $scope.emailAvailable = true;
    $scope.userDetails = {};
    $scope.credentials = {};
    $rootScope.mainWindowAlerts = [];
    $rootScope.authAddRecordBlockAlerts = [];
    $rootScope.settingsAlerts = [];

    $scope.setToken = function (token) {
        setToken(token);
        $scope.$broadcast('authorized', {});
    };

    $scope.checkToken = function(){
        if(getToken() !== null) {
            return Connector.refreshToken(createAuthorizationTokenHeader())
                .then(
                    function (result) {
                        $scope.setToken(result.token);
                        $scope.isAutorized = true;
                    },
                    function () {
                        console.error("Invalid token!");
                        $scope.logout();
                    }
                );
        }
    };
    $scope.checkToken();

    $rootScope.updateToken = function () {
        $scope.checkToken();
    };

    $scope.login = function(credentials){
        $scope.loginSubmitButtomDisabled = true;
        return Connector.login(credentials)
            .then(
                function(result) {
                    $scope.setToken(result.token);
                    $scope.isAutorized = true;
                    $location.path('/user-home');
                    $scope.loginSubmitButtomDisabled = false;
                },
                function(errResponse){
                    $scope.loginSubmitButtomDisabled = false;
                    if(errResponse.error === 'USER_DISABLED'){
                        $scope.model = {};
                        $scope.model.credentials = credentials;
                        $scope.model.scope = $scope;
                        $scope.model.title = 'User Disabled';
                        $scope.model.body = 'User has been disabled and waiting for permanent deletion. If you want revive your account, please, click "Revive" button';
                        AlertsService.openAlertModal('alert-user-revive.html', AlertsService.setModelTitleClassByStatus($scope.model));
                    }else{
                        $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.message);
                    }
                }
            );
    };

    $scope.signup = function(userDetails){
        $scope.signUpSubmitButtomDisabled = true;
        return Connector.signup(userDetails)
            .then(
                function(result) {
                    $scope.setToken(result.token);
                    $scope.isAutorized = true;
                    $location.path('/user-home');
                    $scope.signUpSubmitButtomDisabled = false;
                },
                function(errResponse){
                    $scope.signUpSubmitButtomDisabled = false;
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.message);
                    console.error('Error while fetching dataTable');
                }
            );
    };

    $scope.reviveUser = function(credentials){
        return Connector.reviveUser(credentials)
            .then(
                function(result) {
                    $scope.login(credentials);
                    return result;
                },
                function(errResponse){
                    $rootScope.addAuthAddRecordBlockAlert("danger", errResponse.message);
                }
            );
    };

    var resizeTimer;
    $scope.checkEmail = function (email) {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function () {
            if(typeof email !== 'undefined'){
                Connector.isEmailRegistered(email).then(
                    function (result) {
                        $scope.emailAvailable = !result;
                    },
                    function () {
                        console.error('Error while fetching isEmailRegistered');
                    }
                )

            }
        }, 1000);
    };

    $scope.logout = function(){
        $scope.isAutorized = false;
        $location.path('/');
        removeToken();
    };

    $rootScope.deAuthorize = function () {
        $scope.isAutorized = false;
        removeToken();
    };

    $scope.getVersion = function () {
        return Connector.getVersion()
            .then(
                function (buildInfo) {
                    $scope.buildInfo = buildInfo;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    return null;
                }
            )
    };
    $scope.getVersion();

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

    $rootScope.addSettingsAlert = function (type, msg) {
        $rootScope.settingsAlerts.push({type: type, msg: msg});
    };

    $rootScope.closeSettingsAlert = function (index) {
        $rootScope.settingsAlerts.splice(index, 1);
    };

    $rootScope.authCheck = function () {
        if($scope.isAutorized === false) {
            $scope.logout();
            $rootScope.mainWindowAlerts = [];
            $rootScope.authAddRecordBlockAlerts = [];
        }
    };

    $rootScope.authExceptionCheck = function (errResponse) {
        if(errResponse.error === 'Unauthorized'){
            $scope.isAutorized = false;
            $rootScope.authCheck();
        }
    };
});