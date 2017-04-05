package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.external.ChartRequestWrapper;
import com.kamabizbazti.model.entities.dao.ChartSelection;
import com.kamabizbazti.model.entities.external.ChartWrapper;
import com.kamabizbazti.model.entities.external.EVersion;
import com.kamabizbazti.model.exceptions.UnknownSelectionIdException;

import java.util.List;

public interface IGeneralRequestHandler {
    List<ChartSelection> getGeneralChartSelectionsList();

    List<ChartSelection> getUserChartSelectionsList();

    ChartWrapper getGeneralDatatable(ChartRequestWrapper chartRequestWrapper) throws UnknownSelectionIdException;

    ChartWrapper getDefaultDataTable() throws UnknownSelectionIdException;

    EVersion getVersion();
}