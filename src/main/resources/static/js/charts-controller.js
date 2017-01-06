'use strict';

//noinspection JSUnresolvedVariable
google.charts.load('visualization', {'packages':['corechart']});
//noinspection JSUnresolvedFunction,JSUnresolvedVariable
google.charts.setOnLoadCallback(drawDefaultChart);

var isChartsLoaded = false;

app.controller('ChartsController', function($scope, $http, $q, $rootScope) {
    console.log("ChartsController");
    $scope.containerId = "chart-canvas";
    $scope.chartRequestWrapper = {};
    $scope.startDate = new Date();
    $scope.endDate = new Date();

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
    if(isChartsLoaded){
        $scope.drawInitiateChart();
    }

    $scope.getDataAndDrawChart = function(chartSelection){
        $scope.activeChartSelection = chartSelection;
        $scope.chartRequestWrapper.chartSelection = chartSelection;
        if($scope.activeChartSelection.datePicker) {
            $scope.chartRequestWrapper.startDate = $scope.startDate.getTime();
            $scope.chartRequestWrapper.endDate = $scope.endDate.getTime();
        }
        $scope.getDataTable($scope.chartRequestWrapper).then(
            function(chartWrapper){
                $scope.drawChart(chartWrapper);
            }
        )
    };

    $rootScope.getAndRedrawChart = function () {
        console.log("getAndRedrawChart");
        $scope.chartRequestWrapper.chartSelection = $scope.activeChartSelection;
        if($scope.activeChartSelection.datePicker) {
            $scope.chartRequestWrapper.startDate = $scope.startDate.getTime();
            $scope.chartRequestWrapper.endDate = $scope.endDate.getTime();
        }
        $scope.getDataTable($scope.chartRequestWrapper).then(
            function(chartWrapper){
                $scope.drawChart(chartWrapper);
            }
        )
    };

    $scope.dateOptionsStart = {
        formatYear: 'yy',
        minDate: new Date(2000, 1, 1),
        maxDate: new Date(),
        startingDay: 1
    };

    $scope.dateOptionsEnd = {
        formatYear: 'yy',
        minDate: $scope.startDate,
        maxDate: new Date(),
        startingDay: 1
    };

    $scope.setStartDate = function(startDate){
        $scope.startDate = $scope.checkDateInput(startDate);
        $scope.setMaxDate();
        $scope.dateOptionsEnd.minDate = $scope.startDate;
    };

    $scope.setEndDate = function(endDate){
        $scope.endDate = $scope.checkDateInput(endDate);
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

    var resizeTimer;
    window.onresize = function () {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function () {
            $scope.wrapper.draw();
        }, 250);
    };
});

function drawDefaultChart() {
    console.log("drawDefaultChart");
    //noinspection JSUnresolvedVariable,JSCheckFunctionSignatures
    var scope = angular.element(
        document.getElementById("charts-view-module")).scope();
    //noinspection JSUnresolvedFunction
    scope.$apply(function () {
        scope.drawInitiateChart();
    });
    isChartsLoaded = true;
}