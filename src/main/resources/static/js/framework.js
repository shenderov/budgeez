'use strict';

var app = angular.module('KamaBizbazti', ['ngRoute', 'ngAnimate', 'ui.bootstrap', 'ui.router', 'dialogs.main']);

app.config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('main', {
            url: '/',
            templateUrl: 'pages/main/main-tab.html',
            controller: 'MainController'
        })
        .state('user-home', {
            abstract: true,
            url: '/user-home',
            templateUrl: 'pages/user-home/user-home-tab.html'
        })
        .state('user-home.chart', {
            url: '',
            templateUrl: 'pages/framework/statistics/charts-view-module.html',
            controller: 'UserHomeController'
        })
        .state('user-home.records', {
            url: '/records',
            templateUrl: 'pages/framework/statistics/records-list-module.html'
        });
});
app.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
}]);


