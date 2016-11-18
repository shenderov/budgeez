package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.ChartWrapper;

import java.util.List;

public interface IGeneralRequestHandler {
    List<ChartSelection> getGeneralChartSelectionsList();

    List<ChartSelection> getUserChartSelectionsList();

    ChartWrapper getGeneralDatatable(ChartRequestWrapper chartRequestWrapper);

    ChartWrapper getDefaultDataTable();
}