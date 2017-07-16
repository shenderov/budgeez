package com.budgeez.model.interfaces;

import com.budgeez.model.entities.external.ChartRequestWrapper;
import com.budgeez.model.entities.external.ChartWrapper;

public interface IGeneralStatisticsHandler {
    ChartWrapper getCurrentMonthAverage(ChartRequestWrapper chartRequestWrapper);

    ChartWrapper getNMonthAgoAverage(ChartRequestWrapper chartRequestWrapper, int monthAgo);

    ChartWrapper getLastNMonthsAverageDetailed(ChartRequestWrapper chartRequestWrapper, int monthAgo);
}
