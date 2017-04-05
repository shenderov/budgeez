package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.external.ChartRequestWrapper;
import com.kamabizbazti.model.entities.external.ChartWrapper;
import com.kamabizbazti.model.exceptions.DateRangeException;

public interface IUserStatisticsHandler {
    ChartWrapper getCurrentMonthTotal(ChartRequestWrapper chartRequestWrapper, long userId);

    ChartWrapper getPrevNMonthsAverage(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo);

    ChartWrapper getPrevNMonthsTotal(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo);

    ChartWrapper getCustomPeriodAverage(ChartRequestWrapper chartRequestWrapper, long userId);

    ChartWrapper getCustomPeriodTotal(ChartRequestWrapper chartRequestWrapper, long userId);

    ChartWrapper getLastNMonthsDetailed(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo);

    ChartWrapper getCustomPeriodDetailed(ChartRequestWrapper chartRequestWrapper, long userId) throws DateRangeException;
}
