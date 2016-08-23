package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.ChartWrapper;
import com.kamabizbazti.model.repository.GeneralPurposeRepository;
import com.kamabizbazti.model.repository.RecordRepository;

public interface IGeneralStatisticsHandler {
    ChartWrapper getCurrentMonthAverage(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, ChartRequestWrapper chartRequestWrapper);
    ChartWrapper getNMonthAgoAverage(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, ChartRequestWrapper chartRequestWrapper, int monthAgo);
    ChartWrapper getCustomPeriodAverage(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, ChartRequestWrapper chartRequestWrapper);

//    ChartWrapper getLastThreeMonthAverageDetailed();
//    ChartWrapper getLastSixMonthAverageDetailed();
//    ChartWrapper getLastYearAverageDetailed();
    ChartWrapper getLastNMonthsAverageDetailed(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, ChartRequestWrapper chartRequestWrapper, int monthAgo);
    ChartWrapper getCustomPeriodAverageDetailed(GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository, ChartRequestWrapper chartRequestWrapper);
}
