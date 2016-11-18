package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.GeneralPurpose;

import java.util.List;

public interface IStatisticsHelper {
    List<String> setLabels(List<GeneralPurpose> purposes, boolean others);

    List<Object[]> getGeneralAvgRows(long startDate, long endDate);

    List<Object[]> getUserAvgRows(long userId, long startDate, long endDate);

    List<Object[]> getUserTotalRows(long userId, long startDate, long endDate);

    List<Object[]> getGeneralAvgDetailedRows(List<GeneralPurpose> purposes, int monthAgo);

    List<Object[]> getUserTotalDetailedRows(List<GeneralPurpose> purposes, long userId, int monthAgo);

    List<Object[]> getGeneralAvgDetailedWeeks(List<GeneralPurpose> purposes, ChartRequestWrapper chartRequestWrapper);

    List<Object[]> getGeneralAvgDetailedMonth(List<GeneralPurpose> purposes, ChartRequestWrapper chartRequestWrapper);
}