package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.dao.ChartSelection;
import com.kamabizbazti.model.entities.ChartWrapper;
import com.kamabizbazti.model.exceptions.UnknownSelectionIdException;

import java.util.List;

public interface IGeneralRestController {
    List<ChartSelection> getGeneralChartSelectionsList();

    List<ChartSelection> getUserChartSelectionsList();

    ChartWrapper getDefaultDataTable() throws Exception;

    ChartWrapper getGeneralDataTable(ChartRequestWrapper chartRequestWrapper) throws UnknownSelectionIdException;
}
