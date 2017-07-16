package com.budgeez.model.interfaces;

import com.budgeez.model.entities.external.ChartRequestWrapper;
import com.budgeez.model.entities.external.ChartWrapper;
import com.budgeez.model.exceptions.DateRangeException;

public interface IUserStatisticsHandler {
    ChartWrapper getCurrentMonthTotal(ChartRequestWrapper chartRequestWrapper, long userId);

    ChartWrapper getPrevNMonthsAverage(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo);

    ChartWrapper getPrevNMonthsTotal(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo);

    ChartWrapper getCustomPeriodAverage(ChartRequestWrapper chartRequestWrapper, long userId);

    ChartWrapper getCustomPeriodTotal(ChartRequestWrapper chartRequestWrapper, long userId);

    ChartWrapper getLastNMonthsDetailed(ChartRequestWrapper chartRequestWrapper, long userId, int monthsAgo);

    ChartWrapper getCustomPeriodDetailed(ChartRequestWrapper chartRequestWrapper, long userId) throws DateRangeException;
}
