package com.kamabizbazti.model.handlers;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.helpers.DateHelper;
import com.kamabizbazti.model.helpers.StatisticsHelper;
import com.kamabizbazti.model.interfaces.IGeneralStatisticsHandler;
import com.kamabizbazti.model.repository.GeneralPurposeRepository;
import com.kamabizbazti.model.repository.RecordRepository;

import java.util.*;

public class GeneralStatisticsHandler implements IGeneralStatisticsHandler{

    private static final String [] AVG_LABELS = {"Purpose",  "Amount"};
    private static final String AVG_OTHERS = "Others";
    private static final String LABEL_MONTH_WEEK = "Month/Week";
    private static final String LABEL_MONTH = "Month";
    private static final String LABEL_YEAR = "Year";

    private DateHelper dateHelper = new DateHelper();
    private StatisticsHelper statisticsHelper = new StatisticsHelper();

    @Override
    public ChartWrapper getCurrentMonthAverage(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, ChartRequestWrapper chartRequestWrapper) {
        long startDate = dateHelper.getFirstDayOfCurrentMonth();
        long endDate = dateHelper.getLastDayOfCurrentMonth();
        DataTable data = new DataTable(AVG_LABELS, getAvgRows(generalPurposeRepository, recordRepository, startDate, endDate));
        return new ChartWrapper(ChartType.PIECHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    @Override
    public ChartWrapper getNMonthAgoAverage(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, ChartRequestWrapper chartRequestWrapper, int monthAgo) {
        long startDate = dateHelper.getFirstDayOfNMonthAgo(monthAgo);
        long endDate = dateHelper.getLastDayOfPreviousMonth();
        DataTable data = new DataTable(AVG_LABELS, getAvgRows(generalPurposeRepository, recordRepository, startDate, endDate));
        return new ChartWrapper(ChartType.PIECHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    @Override
    public ChartWrapper getCustomPeriodAverage(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, ChartRequestWrapper chartRequestWrapper) {
        DataTable data = new DataTable(AVG_LABELS, getAvgRows(generalPurposeRepository, recordRepository, chartRequestWrapper.getStartDate(), chartRequestWrapper.getEndDate()));
        return new ChartWrapper(ChartType.PIECHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    @Override
    public ChartWrapper getLastNMonthsAverageDetailed(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, ChartRequestWrapper chartRequestWrapper, int monthAgo) {
        List <String> labels = new ArrayList<>();
        List <Object []> rows = new LinkedList<>();
        List <GeneralPurpose> purposes = generalPurposeRepository.findAll();
        labels.add(LABEL_MONTH);
        for(GeneralPurpose pps : purposes)
            labels.add(pps.getName());
        labels.add(AVG_OTHERS);
        for(int i = 0; i < monthAgo; i++){
            List <Object> row = new LinkedList<>();
            long startDate = dateHelper.getFirstDayOfNMonthAgo(i);
            long endDate = dateHelper.getLastDayOfNMonthAgo(i);
            row.add(dateHelper.getMonthNameByDate(startDate));
            for(GeneralPurpose pps : purposes)
                row.add(recordRepository.averageValueByPurposeId(pps.getPurposeId(), startDate, endDate));
            row.add(recordRepository.averageOfCustomRecords(startDate, endDate));
            rows.add(row.toArray());
        }
        DataTable data = new DataTable(labels, rows);
        return new ChartWrapper(ChartType.COLUMNCHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    @Override
    public ChartWrapper getCustomPeriodAverageDetailed(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, ChartRequestWrapper chartRequestWrapper) {
        return null;
    }

    private List<Object[]> getAvgRows(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, long startDate, long endDate) {
        List <Object[]> rows = new ArrayList<>();
        List <GeneralPurpose> purposes = generalPurposeRepository.findAll();
        Double amount;
        for(GeneralPurpose pps : purposes) {
            amount = recordRepository.averageValueByPurposeId(pps.getPurposeId(), startDate, endDate);
            if(amount != null)
                rows.add(new Object[]{pps.getName(), recordRepository.averageValueByPurposeId(pps.getPurposeId(), startDate, endDate)});
        }
        amount = recordRepository.averageOfCustomRecords(startDate, endDate);
        if(amount != null)
            rows.add(new Object [] {AVG_OTHERS, amount});
        return rows;
    }
}
