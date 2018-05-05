'use strict';

var app = angular.module('Budgeez', ['ngRoute', 'ngAnimate', 'ui.bootstrap', 'ui.router', 'dialogs.main', 'angular.filter']);

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
            templateUrl: 'pages/framework/statistics/records-list-module.html',
            controller: 'RecordListController'
        })
        .state('settings', {
            url: '/settings',
            templateUrl: 'pages/settings/settings-tab.html',
            controller: 'SettingsController'
        })
        .state('confirm', {
            url: '/confirm?token',
            controller: 'ConfirmController'
        })
        .state('verify', {
            url: '/verify?token',
            controller: 'VerifyEmailController'
        })
        .state('resetPassword', {
            url: '/resetPassword?token',
            templateUrl: 'pages/auth/reset-password-tab.html',
            controller: 'ResetPasswordController'
        })
        .state('confirmation-page', {
            url: '/confirmation-page',
            templateUrl: 'pages/system/confirmation-tab.html',
            controller: 'ConfirmationPageCtrl'
        });
});
app.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
}]);