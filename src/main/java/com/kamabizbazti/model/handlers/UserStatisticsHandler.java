package com.kamabizbazti.model.handlers;

import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.exceptions.DateRangeException;
import com.kamabizbazti.model.interfaces.IDateHelper;
import com.kamabizbazti.model.interfaces.IStatisticsHelper;
import com.kamabizbazti.model.interfaces.IUserStatisticsHandler;
import com.kamabizbazti.model.repository.CustomPurposeRepository;
import com.kamabizbazti.model.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class UserStatisticsHandler implements IUserStatisticsHandler {

    @Autowired
    private IDateHelper dateHelper;

    @Autowired
    private IStatisticsHelper statisticsHelper;

    @Autowired
    private CustomPurposeRepository customPurposeRepository;

    @Autowired
    private RecordRepository recordRepository;

    public ChartWrapper getCurrentMonthTotal(ChartRequestWrapper chartRequestWrapper, long userId) {
        long startDate = dateHelper.getFirstDayOfCurrentMonth();
        long endDate = dateHelper.getLastDayOfCurrentMonth();
        DataTable data = new DataTable(statisticsHelper.setLabels(), recordRepository.getTotalForAllActualPurposesForUser(userId, startDate, endDate));
        return new ChartWrapper(ChartType.PIECHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    public ChartWrapper getPrevNMonthsAverage(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo) {
        long startDate = dateHelper.getFirstDayOfNMonthAgo(monthsAgo);
        long endDate = dateHelper.getLastDayOfPreviousMonth();
        DataTable data = new DataTable(statisticsHelper.setLabels(), recordRepository.getAverageForAllActualPurposesForUser(userId, startDate, endDate));
        return new ChartWrapper(ChartType.PIECHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    public ChartWrapper getPrevNMonthsTotal(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo) {
        long startDate = dateHelper.getFirstDayOfNMonthAgo(monthsAgo);
        long endDate = dateHelper.getLastDayOfPreviousMonth();
        DataTable data = new DataTable(statisticsHelper.setLabels(), recordRepository.getTotalForAllActualPurposesForUser(userId, startDate, endDate));
        return new ChartWrapper(ChartType.PIECHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    public ChartWrapper getCustomPeriodAverage(ChartRequestWrapper chartRequestWrapper, long userId) {
        DataTable data = new DataTable(statisticsHelper.setLabels(), recordRepository.getAverageForAllActualPurposesForUser(userId, chartRequestWrapper.getStartDate(), chartRequestWrapper.getEndDate()));
        return new ChartWrapper(ChartType.PIECHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    public ChartWrapper getCustomPeriodTotal(ChartRequestWrapper chartRequestWrapper, long userId) {
        DataTable data = new DataTable(statisticsHelper.setLabels(), recordRepository.getTotalForAllActualPurposesForUser(userId, chartRequestWrapper.getStartDate(), chartRequestWrapper.getEndDate()));
        return new ChartWrapper(ChartType.PIECHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    public ChartWrapper getLastNMonthsDetailed(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo) {
        List<GeneralPurpose> purposes = customPurposeRepository.getAllActualUserPurposes(userId);
        boolean isTheSameYear = dateHelper.getYear(dateHelper.getLastDayOfPreviousMonth()).equals(dateHelper.getYear(dateHelper.getFirstDayOfNMonthAgo(monthsAgo)));
        DataTable data = new DataTable(statisticsHelper.setLabelsForPurposes(purposes, false), getGeneralAvgDetailedNMonthsAgo(purposes, userId, monthsAgo, isTheSameYear));
        return new ChartWrapper(ChartType.COLUMNCHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());
    }

    public ChartWrapper getCustomPeriodDetailed(ChartRequestWrapper chartRequestWrapper, long userId) throws DateRangeException {
        List<GeneralPurpose> purposes = customPurposeRepository.getAllActualUserPurposes(userId);
        List<Object[]> rows = null;
        boolean isTheSameYear = dateHelper.getYear(chartRequestWrapper.getStartDate()).equals(chartRequestWrapper.getEndDate());
        if (dateHelper.weeksBetweenTwoDates(chartRequestWrapper.getStartDate(), chartRequestWrapper.getEndDate()) + 1 <= 12) {
            rows = getGeneralAvgDetailedWeeks(purposes, userId, chartRequestWrapper, isTheSameYear);
        } else if (dateHelper.monthsBetweenTwoDates(chartRequestWrapper.getStartDate(), chartRequestWrapper.getEndDate()) + 1 <= 12) {
            rows = getGeneralAvgDetailedMonths(purposes, userId, chartRequestWrapper, isTheSameYear);
        } else if (dateHelper.yearsBetweenTwoDates(chartRequestWrapper.getStartDate(), chartRequestWrapper.getEndDate()) <= 12) {
            rows = getGeneralAvgDetailedYears(purposes, userId, chartRequestWrapper);
        }
        DataTable data = new DataTable(statisticsHelper.setLabelsForPurposes(purposes, false), rows);
        return new ChartWrapper(ChartType.COLUMNCHART, data.getDataTableAsArray(), chartRequestWrapper.getChartSelection().getTitle());

    }

    private List<Object[]> getGeneralAvgDetailedWeeks(List<GeneralPurpose> purposes, long userId, ChartRequestWrapper chartRequestWrapper, boolean isTheSameYear) throws DateRangeException {
        int weeksCount = (int) dateHelper.weeksBetweenTwoDates(chartRequestWrapper.getStartDate(), chartRequestWrapper.getEndDate()) + 1;
        if (weeksCount < 2)
            throw new DateRangeException("DATE_RANGE_IS_TOO_SHORT");
        List<Object[]> rows = new ArrayList<>();
        List<Object> row = new LinkedList<>();
        long startDate = dateHelper.getFirstDayOfWeekByDate(chartRequestWrapper.getEndDate());
        long endDate = chartRequestWrapper.getEndDate();
        row.add(dateHelper.getFormattedWeekNameByDate(isTheSameYear, startDate, weeksCount));
        row.addAll(getAvgDetailedRowsInRange(purposes, userId, startDate, endDate));
        rows.add(row.toArray());
        for (int i = 0; i < weeksCount - 2; i++) {
            row = new LinkedList<>();
            startDate = dateHelper.getFirstDayOfNWeeksAgoFromDate(startDate, 1);
            endDate = dateHelper.getLastDayOfWeekByDate(startDate);
            row.add(dateHelper.getFormattedWeekNameByDate(isTheSameYear, startDate, weeksCount));
            row.addAll(getAvgDetailedRowsInRange(purposes, userId, startDate, endDate));
            rows.add(row.toArray());
        }
        row = new LinkedList<>();
        startDate = chartRequestWrapper.getStartDate();
        endDate = dateHelper.getLastDayOfWeekByDate(startDate);
        row.add(dateHelper.getFormattedWeekNameByDate(isTheSameYear, startDate, weeksCount));
        row.addAll(getAvgDetailedRowsInRange(purposes, userId, startDate, endDate));
        rows.add(row.toArray());
        return rows;
    }

    private List<Object[]> getGeneralAvgDetailedMonths(List<GeneralPurpose> purposes, long userId, ChartRequestWrapper chartRequestWrapper, boolean isTheSameYear) {
        int monthsCount = (int) dateHelper.monthsBetweenTwoDates(chartRequestWrapper.getStartDate(), chartRequestWrapper.getEndDate());
        List<Object[]> rows = new ArrayList<>();
        List<Object> row = new LinkedList<>();
        long startDate = dateHelper.getFirstDayOfMonthByDate(chartRequestWrapper.getEndDate());
        long endDate = chartRequestWrapper.getEndDate();
        row.add(dateHelper.getFormattedMonthNameByDate(isTheSameYear, startDate, monthsCount));
        row.addAll(getAvgDetailedRowsInRange(purposes, userId, startDate, endDate));
        rows.add(row.toArray());
        for (int i = 0; i <= monthsCount - 2; i++) {
            row = new LinkedList<>();
            startDate = dateHelper.getFirstDayOfMonthAgoFromDate(startDate, 1);
            endDate = dateHelper.getLastDayOfMonthByDate(startDate);
            row.add(dateHelper.getFormattedMonthNameByDate(isTheSameYear, startDate, monthsCount));
            row.addAll(getAvgDetailedRowsInRange(purposes, userId, startDate, endDate));
            rows.add(row.toArray());
        }
        row = new LinkedList<>();
        startDate = chartRequestWrapper.getStartDate();
        endDate = dateHelper.getLastDayOfMonthByDate(startDate);
        row.add(dateHelper.getFormattedMonthNameByDate(isTheSameYear, startDate, monthsCount));
        row.addAll(getAvgDetailedRowsInRange(purposes, userId, startDate, endDate));
        rows.add(row.toArray());
        return rows;
    }

    private List<Object[]> getGeneralAvgDetailedYears(List<GeneralPurpose> purposes, long userId, ChartRequestWrapper chartRequestWrapper) throws DateRangeException {
        int yearsCount = (int) dateHelper.yearsBetweenTwoDates(chartRequestWrapper.getStartDate(), chartRequestWrapper.getEndDate());
        if (yearsCount >= 12)
            throw new DateRangeException("DATE_RANGE_IS_TOO_LONG");
        List<Object[]> rows = new ArrayList<>();
        List<Object> row = new LinkedList<>();
        long startDate = dateHelper.getFirstDayOfYearByDate(chartRequestWrapper.getEndDate());
        long endDate = chartRequestWrapper.getEndDate();
        row.add(dateHelper.getYear(startDate));
        row.addAll(getAvgDetailedRowsInRange(purposes, userId, startDate, endDate));
        rows.add(row.toArray());
        for (int i = 0; i <= yearsCount - 2; i++) {
            row = new LinkedList<>();
            startDate = dateHelper.getFirstDayOfNYearsAgoFromDate(startDate, 1);
            endDate = dateHelper.getLastDayOfYearByDate(startDate);
            row.add(dateHelper.getYear(startDate));
            row.addAll(getAvgDetailedRowsInRange(purposes, userId, startDate, endDate));
            rows.add(row.toArray());
        }
        row = new LinkedList<>();
        startDate = chartRequestWrapper.getStartDate();
        endDate = dateHelper.getLastDayOfYearByDate(startDate);
        row.add(dateHelper.getYear(startDate));
        row.addAll(getAvgDetailedRowsInRange(purposes, userId, startDate, endDate));
        rows.add(row.toArray());
        return rows;
    }

    private List<Object[]> getGeneralAvgDetailedNMonthsAgo(List<GeneralPurpose> purposes, long userId, int monthsAgo, boolean isTheSameYear) {
        List<Object[]> rows = new ArrayList<>();
        for (int i = monthsAgo; i > 0; i--) {
            List<Object> row = new LinkedList<>();
            long startDate = dateHelper.getFirstDayOfNMonthAgo(i);
            long endDate = dateHelper.getLastDayOfNMonthAgo(i);
            row.add(dateHelper.getFormattedMonthNameByDate(isTheSameYear, startDate, monthsAgo));
            row.addAll(getAvgDetailedRowsInRange(purposes, userId, startDate, endDate));
            rows.add(row.toArray());
        }
        return rows;
    }

    private List<Object> getAvgDetailedRowsInRange(List<GeneralPurpose> purposes, long userId, long startDate, long endDate) {
        List<Object> row = new LinkedList<>();
        for (GeneralPurpose pps : purposes) {
            Double res = recordRepository.averageValueByPurposeIdAndUserId(pps.getPurposeId(), userId, startDate, endDate);
            if (res == null)
                row.add(0);
            else
                row.add(res);
        }
        return row;
    }
}
