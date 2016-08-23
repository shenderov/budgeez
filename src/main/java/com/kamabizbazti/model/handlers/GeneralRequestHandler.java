package com.kamabizbazti.model.handlers;

import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.interfaces.IGeneralPurposeRepository;
import com.kamabizbazti.model.interfaces.IGeneralRequestHandler;
import com.kamabizbazti.model.repository.ChartSelectionRepository;
import com.kamabizbazti.model.repository.GeneralPurposeRepository;
import com.kamabizbazti.model.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

public class GeneralRequestHandler implements IGeneralRequestHandler{

    //private static final String CHART_DIV_ID = "chart-canvas";

    public ChartWrapper getGeneralDatatable(ChartRequestWrapper chartRequestWrapper, GeneralStatisticsHandler generalStatisticsHandler, GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository){
        ChartWrapper wrapper = null;
        switch(chartRequestWrapper.getChartSelection().getSelectionId()){
            case CURRENT_MONTH_AVG:
                wrapper = generalStatisticsHandler.getCurrentMonthAverage(generalPurposeRepository, recordRepository, chartRequestWrapper);
                break;
            case PREV_MONTH_AVG:
                wrapper = generalStatisticsHandler.getNMonthAgoAverage(generalPurposeRepository, recordRepository, chartRequestWrapper, 1);
                break;
            case PREV_THREE_MONTH_AVG:
                wrapper = generalStatisticsHandler.getNMonthAgoAverage(generalPurposeRepository, recordRepository, chartRequestWrapper, 3);
                break;
            case CUSTOM_PERIOD_AVG:
                wrapper = generalStatisticsHandler.getCustomPeriodAverage(generalPurposeRepository, recordRepository, chartRequestWrapper);
                break;
            case LAST_THREE_MONTH_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getLastNMonthsAverageDetailed(generalPurposeRepository, recordRepository, chartRequestWrapper, 3);
                break;
            case LAST_SIX_MONTH_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getLastNMonthsAverageDetailed(generalPurposeRepository, recordRepository, chartRequestWrapper, 6);
                break;
            case LAST_YEAR_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getLastNMonthsAverageDetailed(generalPurposeRepository, recordRepository, chartRequestWrapper, 12);
                break;
            case CUSTOM_PERIOD_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getCustomPeriodAverageDetailed(generalPurposeRepository, recordRepository, chartRequestWrapper);
                break;
            default:
                break;
        }
        return wrapper;
//        ChartWrapper wrapper = null;
//        List<Object[]> rows;
//        DataTable data = new DataTable();
//        switch(selection.getSelectionId()){
//            case "currentMonthAvg":
//            case "prevMonthAvg":
//            case "prevThreeMonthAvg":
//            case "customPeriodAvg":
//                data.setLabels(getLabelsByChartType(selection));
//                rows = new LinkedList<Object[]>();
//                rows.add(new Object [] {"Food", getRndomInt(100, 1000)});
//                rows.add(new Object [] {"Bills", getRndomInt(100, 1000)});
//                rows.add(new Object [] {"Travel", getRndomInt(100, 1000)});
//                rows.add(new Object [] {"Beer", getRndomInt(100, 1000)});
//                rows.add(new Object [] {"Gas", getRndomInt(100, 1000)});
//                rows.add(new Object [] {"Pubs", getRndomInt(100, 1000)});
//                rows.add(new Object [] {"Fun", getRndomInt(100, 1000)});
//                data.setRows(rows);
//                wrapper = new ChartWrapper(selection.getChartType(), data.getDataTableAsArray(), getOptionsByChartType(selection), CHART_DIV_ID);
//                break;
//            case "lastThreeMonthAvg":
//            case "customPeriodAvgDetailed":
//            case "lastSixMonthAvg":
//            case "lastYearAvg":
//                data.setLabels(getLabelsByChartType(selection));
//                data.setRows(getComboRows(selection, data.getLabelsAsArray().length));
//                wrapper = new ChartWrapper(selection.getChartType(), data.getDataTableAsArray(), getOptionsByChartType(selection), CHART_DIV_ID);
//                break;
//        }
//        System.out.println("Wrapper: " + wrapper);
//        System.out.println("DataTable: " + data.toString());
//        return wrapper;
    }

    public ChartWrapper getDefaultDataTable(ChartSelectionRepository chartSelectionRepository, GeneralStatisticsHandler generalStatisticsHandler, GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository) {
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(chartSelectionRepository.findOne(ChartSelectionId.CURRENT_MONTH_AVG));
        return getGeneralDatatable(requestWrapper, generalStatisticsHandler, generalPurposeRepository, recordRepository);
        //return getGeneralDatatable(new ChartSelection("currentMonthAvg", "Current Month Average", ChartType.PIECHART, false, false));
    }

    public List<ChartSelection> getGeneralChartSelectionsList(ChartSelectionRepository chartSelectionRepository) {
//        List <ChartSelection> chartSelections = new ArrayList<>();
//        chartSelections.add(new ChartSelection("curentMonthAvg", "Current Month Average", "PieChart", false, false));
//        chartSelections.add(new ChartSelection("prevMonthAvg", "Previous Month Average", "PieChart", false, false));
//        chartSelections.add(new ChartSelection("prevThreeMonthAvg", "Previous 3 Month Average", "PieChart", false, false));
//        chartSelections.add(new ChartSelection("customPeriodAvg", "Custom Periode Average", "PieChart", true, false));
//        chartSelections.add(new ChartSelection("lastThreeMonthAvg", "Last 3 Month Average Detailed", "ColumnChart", false, false));
//        chartSelections.add(new ChartSelection("lastSixMonthAvg", "Last 6 Month Average Detailed", "ColumnChart", false, false));
//        chartSelections.add(new ChartSelection("lastYearAvg", "Last Year Average Detailed", "ColumnChart", false, false));
//        chartSelections.add(new ChartSelection("customPeriodAvgDetailed", "Custom Periode Average Detailed", "ColumnChart", true, false));
//        return chartSelections;
        return chartSelectionRepository.findAllGeneralSelections();
    }



    private int getRndomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

//    private String[] getLabelsByChartType(ChartSelection selection) throws Exception {
//        if(selection.getChartType().equals(ChartType.PIECHART)){
//            String [] lbs = {"Purpose",  "Amount"};
//            return lbs;
//        }else if (selection.getChartType().equals(ChartType.COLUMNCHART)){
//            String [] lbs = {"Month",  "Food", "Bills", "Travel", "Beer", "Gas", "Pubs", "Fun"};
//            return lbs;
//        }else
//            throw new Exception("Type " + selection.getChartType() + " is not supported");
//    }

    private Map<String, Object> getOptionsByChartType(ChartSelection selection) throws Exception {
        Map <String, Object> options = new HashMap<>();

        if(selection.getChartType().equals(ChartType.PIECHART)){
            options.put("title", "Current Month Average");
        }else if (selection.getChartType().equals(ChartType.COLUMNCHART)){
            options.put("title", selection.getTitle());
            options.put("isStacked", true);
            Map<String, String> vAxis = new HashMap<>();
            vAxis.put("title", "USD");
            options.put("vAxis", vAxis);
            Map<String, String> hAxis = new HashMap<>();
            hAxis.put("title", "Month");
            options.put("hAxis", hAxis);
        }else
            throw new Exception("Type " + selection.getChartType() + " is not supported");
        return options;
    }

//    private List<Object[]> getComboRows(ChartSelection selection, int length) {
//        List <Object []> rows = new LinkedList<>();
//        Calendar cal =  Calendar.getInstance();
//        int monthsCount = 1;
//        switch(selection.getSelectionId()){
//            case "lastThreeMonthAvg":
//            case "lastThreeMonthDetailed":
//                monthsCount = 3;
//                break;
//            case "lastSixMonthAvg":
//            case "lastSixMonthDetailed":
//                monthsCount = 6;
//                break;
//            case "lastYearAvg":
//            case "lastYearDetailed":
//                monthsCount = 12;
//                break;
//            case "customPeriodAvgDetailed":
//            case "customPeriodDetailed":
//                monthsCount = 8;
//                break;
//            default:
//                monthsCount = 0;
//                break;
//        }
//        for(int i = 0; i < monthsCount; i++){
//            List <Object> row = new LinkedList<>();
//            row.add(new SimpleDateFormat("MMMM").format(cal.getTime()));
//            for(int j = 0; j < length-1; j++)
//                row.add(getRndomInt(100, 1000));
//            rows.add(row.toArray());
//            cal.add(Calendar.MONTH ,-1);
//        }
//        return rows;
//    }





}
