package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.ChartWrapper;

public interface IUserStatisticsHandler {
    ChartWrapper getCurrentMonth(ChartRequestWrapper chartRequestWrapper, long userId);

    ChartWrapper getPrevNMonthsAverage(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo);

    ChartWrapper getPrevNMonthsTotal(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo);

    ChartWrapper getCustomPeriodAverage(ChartRequestWrapper chartRequestWrapper, long userId);

    ChartWrapper getCustomPeriodTotal(ChartRequestWrapper chartRequestWrapper, long userId);

    ChartWrapper getLastNMonthsDetailed(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo);

    ChartWrapper getCustomPeriodDetailed(ChartRequestWrapper chartRequestWrapper, long userId);
}
