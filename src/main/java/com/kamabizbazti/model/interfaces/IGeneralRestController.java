package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.external.ChartRequestWrapper;
import com.kamabizbazti.model.entities.dao.ChartSelection;
import com.kamabizbazti.model.entities.external.ChartWrapper;
import com.kamabizbazti.model.entities.external.EVersion;
import com.kamabizbazti.model.exceptions.UnknownSelectionIdException;

import java.util.List;

public interface IGeneralRestController {
    List<ChartSelection> getGeneralChartSelectionsList();

    List<ChartSelection> getUserChartSelectionsList();

    ChartWrapper getDefaultDataTable() throws Exception;

    ChartWrapper getGeneralDataTable(ChartRequestWrapper chartRequestWrapper) throws UnknownSelectionIdException;

    EVersion getGeneralDataTable();
}
