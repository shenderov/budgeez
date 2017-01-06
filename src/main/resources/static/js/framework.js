'use strict';

var app = angular.module('KamaBizbazti', ['ngRoute', 'ngAnimate', 'ui.bootstrap', 'ui.router', 'dialogs.main']);

//app.config(function ($routeProvider) {
app.config(function ($stateProvider) {
    var states = [
        {
            name: 'main',
            url: '/',
            templateUrl: 'pages/main/main-tab.html',
            controller: 'MainController'
        },
        {
            name: 'user-home',
            url: '/user-home',
            templateUrl: 'pages/user-home/user-home-tab.html',
            resolve: {
                people: function() {
                    console.log("user-home");
                    return null;
                }
            },
            controller: 'UserHomeController'
        },
        {
            name: 'user-home.records-list',
            url: '/records-list',
            //templateUrl: 'pages/user-home/user-home-tab.html',
            resolve: {
                people: function() {
                    console.log("user-home.records-list");
                    return null;
                }
            },
           // controller: 'RecordListController'
        },




        {
            name: 'settings',
            url: '/settings',
            templateUrl: 'pages/settings/settings-tab.html',
            controller: 'SettingsController'
        }
    ];
    states.forEach(function(state) {
        $stateProvider.state(state);
    });



    // $routeProvider
    //     .when('/', {
    //         templateUrl: 'pages/main/main-tab.html',
    //         controller: 'MainController'
    //     })
    //     .when('/user-home', {
    //         templateUrl: 'pages/user-home/user-home-tab.html',
    //         controller: 'UserHomeController'
    //     })
    //     .when('/settings', {
    //         templateUrl: 'pages/settings/settings-tab.html',
    //         controller: 'SettingsController'
    //     })
    //     .otherwise({redirectTo: '/'});
});







app.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
}]);










