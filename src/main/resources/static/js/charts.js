'use strict';

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawDefaultChart);
var isChartsLoaded = false;

app.controller('ChartsController', function($scope, $http, $q, $rootScope, Connector, $timeout) {
    console.log("ChartsController");
    $scope.containerId = "chart-canvas";
    $scope.chartRequestWrapper = {};
    $scope.startDate = new Date();
    $scope.endDate = new Date();

    $scope.getDataAndDrawChart = function(chartSelection){
        $scope.selector(chartSelection);

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

    $scope.wrapper = null;
    $scope.drawChart = function(chartWrapper){
        var chartype = null;
        var options = null;
        if(chartWrapper.chartType == "PIECHART"){
            chartype = "PieChart";
            options = {
                title: chartWrapper.title
            };
        }else if (chartWrapper.chartType == "COLUMNCHART"){
            chartype = "ColumnChart";
            options = {
                title: chartWrapper.title,
                vAxis: {
                    title: chartWrapper.vAxis
                },
                hAxis: {
                    title: chartWrapper.hAxis
                },
                isStacked: true
            };
        }
            $scope.wrapper = new google.visualization.ChartWrapper({
                chartType: chartype,
                dataTable: chartWrapper.dataTable,
                options: options,
                containerId: $scope.containerId
        });
        $scope.wrapper.draw();
    };

    $scope.redrawChart = function () {
        console.log("redraw");
        $scope.wrapper.draw();
    };
	
    $scope.selector = function(chartSelection){
        $scope.activeChartSelection = chartSelection;
        // if(!$scope.activeChartSelection.datePicker) {
        //     $scope.startDate = null;
        //     $scope.endDate = null;
        // }
        // if($scope.activeChartSelection.datePicker){
        //     $scope.startDate = new Date();
        //     $scope.endDate = new Date();
        // }else if(!$scope.activeChartSelection.datePicker){
        //     $scope.startDate = null;
        //     $scope.endDate = null;
        // }
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
});

function drawDefaultChart() {
 //   if(!isChartsLoaded){
        console.log("drawDefaultChart");
        var scope = angular.element(
            document.
            getElementById("charts-view-module")).
        scope();
        scope.$apply(function () {
            scope.drawInitiateChart();
        });
        isChartsLoaded = true;
  //  }
}