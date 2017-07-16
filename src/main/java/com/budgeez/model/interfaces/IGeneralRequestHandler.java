package com.budgeez.model.interfaces;

import com.budgeez.model.entities.dao.Currency;
import com.budgeez.model.entities.dao.Language;
import com.budgeez.model.entities.external.ChartRequestWrapper;
import com.budgeez.model.entities.dao.ChartSelection;
import com.budgeez.model.entities.external.ChartWrapper;
import com.budgeez.model.entities.external.EVersion;
import com.budgeez.model.exceptions.UnknownSelectionIdException;

import java.util.List;

public interface IGeneralRequestHandler {
    List<ChartSelection> getGeneralChartSelectionsList();

    List<ChartSelection> getUserChartSelectionsList();

    ChartWrapper getGeneralDatatable(ChartRequestWrapper chartRequestWrapper) throws UnknownSelectionIdException;

    ChartWrapper getDefaultDataTable() throws UnknownSelectionIdException;

    EVersion getVersion();

    List<Language> getLanguages();

    List<Currency> getCurrencies();
}