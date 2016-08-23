package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.ChartWrapper;
import com.kamabizbazti.model.handlers.GeneralStatisticsHandler;
import com.kamabizbazti.model.repository.ChartSelectionRepository;
import com.kamabizbazti.model.repository.GeneralPurposeRepository;
import com.kamabizbazti.model.repository.RecordRepository;

import java.util.List;

public interface IGeneralRequestHandler {
   // List<GeneralPurpose> getPurposesList();
    List<ChartSelection> getGeneralChartSelectionsList(ChartSelectionRepository chartSelectionRepository);
    ChartWrapper getGeneralDatatable(ChartRequestWrapper chartRequestWrapper, GeneralStatisticsHandler generalStatisticsHandler, GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository);
    ChartWrapper getDefaultDataTable(ChartSelectionRepository chartSelectionRepository, GeneralStatisticsHandler generalStatisticsHandler, GeneralPurposeRepository generalPurposeRepository, RecordRepository recordRepository);

}
