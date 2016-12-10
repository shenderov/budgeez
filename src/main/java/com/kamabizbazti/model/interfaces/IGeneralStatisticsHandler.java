package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.ChartWrapper;

public interface IGeneralStatisticsHandler {
    ChartWrapper getCurrentMonthAverage(ChartRequestWrapper chartRequestWrapper);

    ChartWrapper getNMonthAgoAverage(ChartRequestWrapper chartRequestWrapper, int monthAgo);

    ChartWrapper getLastNMonthsAverageDetailed(ChartRequestWrapper chartRequestWrapper, int monthAgo);
}
