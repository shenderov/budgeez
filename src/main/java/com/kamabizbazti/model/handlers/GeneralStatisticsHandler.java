package com.kamabizbazti.model.handlers;

import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.interfaces.IDateHelper;
import com.kamabizbazti.model.interfaces.IGeneralStatisticsHandler;
import com.kamabizbazti.model.interfaces.IStatisticsHelper;
import com.kamabizbazti.model.repository.GeneralPurposeRepository;
import com.kamabizbazti.model.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GeneralStatisticsHandler implements IGeneralStatisticsHandler {

    @Autowired
    private IDateHelper dateHelper;

    @Autowired
    private IStatisticsHelper statisticsHelper;

    @Autowired
    private GeneralPurposeRepository generalPurposeRepository;

    @Autowired
    private RecordRepository recordRepository;

    private static final String[] AVG_LABELS = {"Purpose", "Amount"};
    private static final String AVG_OTHERS = "Others";
    private static final String LABEL_MONTH_WEEK = "Month/Week";
    private static final String LABEL_MONTH = "Month";
    private static final String LABEL_YEAR = "Year";

    public ChartWrapper getCurrentMonthAverage(ChartRequestWrapper chartRequestWrapper) {
        long startDate = dateHelper.getFirstDayOfCurrentMonth();
        long endDate = dateHelper.getLastDayOfCurrentMonth();
        DataTable data = new DataTable(AVG_LABELS, getGeneralAvgRows(startDate, endDate));
        return new ChartWrapper(ChartType.PIECHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    public ChartWrapper getNMonthAgoAverage(ChartRequestWrapper chartRequestWrapper, int monthAgo) {
        long startDate = dateHelper.getFirstDayOfNMonthAgo(monthAgo);
        long endDate = dateHelper.getLastDayOfPreviousMonth();
        DataTable data = new DataTable(AVG_LABELS, getGeneralAvgRows(startDate, endDate));
        return new ChartWrapper(ChartType.PIECHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }


    public ChartWrapper getLastNMonthsAverageDetailed(ChartRequestWrapper chartRequestWrapper, int monthAgo) {
        List<GeneralPurpose> purposes = generalPurposeRepository.getAllActualGeneralPurposes();
        boolean isCustomRecordsExist = recordRepository.averageOfCustomRecords(dateHelper.getFirstDayOfNMonthAgo(monthAgo), dateHelper.getLastDayOfPreviousMonth()) != null;
        DataTable data = new DataTable(statisticsHelper.setLabelsForPurposes(purposes, isCustomRecordsExist), getGeneralAvgDetailedRowsNMonthAgo(purposes, monthAgo, isCustomRecordsExist));
        return new ChartWrapper(ChartType.COLUMNCHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    private List<Object[]> getGeneralAvgRows(long startDate, long endDate) {
        List<Object[]> rows = recordRepository.getAverageForAllPurposes(startDate, endDate);
        return rows;
    }

    private List<Object[]> getGeneralAvgDetailedRowsNMonthAgo(List<GeneralPurpose> purposes, int monthAgo, boolean others) {
        List<Object[]> rows = new ArrayList<>();
        boolean isTheSameYear = dateHelper.getYear(dateHelper.getLastDayOfPreviousMonth()).equals(dateHelper.getYear(dateHelper.getFirstDayOfNMonthAgo(monthAgo)));
        for (int i = monthAgo; i > 0; i--) {
            List<Object> row = new LinkedList<>();
            long startDate = dateHelper.getFirstDayOfNMonthAgo(i);
            long endDate = dateHelper.getLastDayOfNMonthAgo(i);
            row.add(dateHelper.getFormattedMonthNameByDate(isTheSameYear, startDate, monthAgo));
            for (GeneralPurpose pps : purposes) {
                Double res = recordRepository.averageValueByPurposeId(pps.getPurposeId(), startDate, endDate);
                if (res == null)
                    row.add(0);
                else
                    row.add(res);
            }
            if (others)
                row.add(recordRepository.averageOfCustomRecords(startDate, endDate));
            rows.add(row.toArray());
        }
        return rows;
    }
}
