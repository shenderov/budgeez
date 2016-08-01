package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.ChartWrapper;
import com.kamabizbazti.model.entities.GeneralPurpose;

import java.util.List;

public interface IGeneralRequestHandler {
    List<GeneralPurpose> getPurposesList();
    List<ChartSelection> getGeneralChartSelectionsList();
    ChartWrapper getGeneralDatatable(ChartSelection selection) throws Exception;
    ChartWrapper getDefaultDataTable() throws Exception;

}
