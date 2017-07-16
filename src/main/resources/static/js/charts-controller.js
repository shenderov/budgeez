'use strict';

//noinspection JSUnresolvedVariable
google.charts.load('visualization', {'packages':['corechart']});
//noinspection JSUnresolvedFunction,JSUnresolvedVariable
google.charts.setOnLoadCallback(drawDefaultChart);

var isChartsLoaded = false;
var chartAuth = false;

app.controller('ChartsController', function($scope, $http, $q, $rootScope) {
    var date = new Date();
    $scope.containerId = "chart-canvas";
    $scope.chartRequestWrapper = {};
    $scope.chartRequestWrapper.chartSelection = {};
    $scope.chartRequestWrapper.datePicker = {};
    $scope.datePickerSubmitButtonName = "Get Chart";
    $scope.startDate = new Date(date.getFullYear(), date.getMonth(), 1);
    $scope.endDate = new Date(date.getFullYear(), date.getMonth(), date.getDate(), 23, 59, 59);

    $scope.drawChart = function(chartWrapper){
        var chartType;
        var options;
        if (chartWrapper.chartType == "PIECHART"){
            chartType = "PieChart";
            options = {
                title: chartWrapper.title,
                titleTextStyle: {
                    fontSize: 18
                },
                legend: {
                    position: 'bottom',
                    maxLines: 5,
                    alignment: 'center'
                },
                pieSliceText: 'label'
            };
        }else if (chartWrapper.chartType == "COLUMNCHART"){
            chartType = "ColumnChart";
            options = {
                title: chartWrapper.title,
                titleTextStyle: {
                    fontSize: 18
                },
                vAxis: {
                    title: chartWrapper.vAxis
                },
                hAxis: {
                    title: chartWrapper.hAxis
                },
                isStacked: true,
                legend: {
                    position: 'bottom',
                    maxLines: 5,
                    alignment: 'center'
                },
                animation: {
                    duration: 1000,
                    startup: true
                }
            };
        }
        //noinspection JSUnresolvedVariable,JSUnresolvedFunction
        $scope.wrapper = new google.visualization.ChartWrapper({
            chartType: chartType,
            dataTable: chartWrapper.dataTable,
            options: options,
            containerId: $scope.containerId
        });
        $scope.wrapper.draw();
    };

    $scope.drawInitiateChart = function(){
        $scope.getDefaultDataTable().then(
            function(defaultChartWrapper){
                $scope.drawChart(defaultChartWrapper);
            }
        )
    };
    if(isChartsLoaded && chartAuth){
        $scope.drawInitiateChart();
    }

    $scope.getDataAndDrawChart = function(chartSelection){
        $scope.activeChartSelection = chartSelection;
        $scope.chartRequestWrapper.chartSelection = chartSelection;
        if($scope.activeChartSelection.datePicker) {
            $scope.chartRequestWrapper.datePicker.startDate = $scope.startDate.getTime();
            $scope.chartRequestWrapper.datePicker.endDate = $scope.endDate.getTime();
        }
        $scope.getDataTable($scope.chartRequestWrapper).then(
            function(chartWrapper){
                $scope.drawChart(chartWrapper);
            }
        )
    };

    $scope.defaultAction = function () {
        $scope.getDataAndDrawChart($scope.activeChartSelection);
    };

    $scope.$on('defaultAddRecordAction', function () {
        $scope.defaultAction();
    });

    $scope.setActiveChartSelection = function(chartSelection){
        $scope.activeChartSelection = chartSelection;
    };

    $rootScope.getAndRedrawChart = function () {
        $scope.chartRequestWrapper.chartSelection = $scope.activeChartSelection;
        if($scope.activeChartSelection.datePicker) {
            $scope.chartRequestWrapper.datePicker.startDate = $scope.startDate.getTime();
            $scope.chartRequestWrapper.datePicker.endDate = $scope.endDate.getTime();
        }
        $scope.getDataTable($scope.chartRequestWrapper).then(
            function(chartWrapper){
                $scope.drawChart(chartWrapper);
            }
        )
    };

    $scope.dateOptionsStart = {
        formatYear: 'yy',
        maxDate: $scope.endDate,
        startingDay: 1
    };

    $scope.dateOptionsEnd = {
        formatYear: 'yy',
        minDate: $scope.startDate,
        startingDay: 1
    };

    $scope.setStartDate = function(startDate){
        $scope.startDate = startDate;
        $scope.validateStartBeforeEnd();
        $scope.dateOptionsEnd.minDate = $scope.startDate;
    };

    $scope.setEndDate = function(endDate){
        $scope.endDate = endDate;
        $scope.validateStartBeforeEnd();
        $scope.dateOptionsStart.maxDate = $scope.endDate;
    };

    $scope.validateStartBeforeEnd = function(){
        if($scope.startDate > $scope.endDate){
            $scope.endDate = new Date($scope.startDate.getFullYear(), $scope.startDate.getMonth(), $scope.startDate.getDate(), 23, 59, 59);
        }
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

    var resizeTimer;
    window.onresize = function () {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function () {
            $scope.wrapper.draw();
        }, 250);
    };
});

function drawDefaultChart() {
    if(chartAuth) {
        //noinspection JSUnresolvedVariable,JSCheckFunctionSignatures
        var scope = angular.element(
            document.getElementById("charts-view-module")).scope();
        //noinspection JSUnresolvedFunction
        scope.$apply(function () {
            scope.drawInitiateChart();
        });
    }
    isChartsLoaded = true;
}