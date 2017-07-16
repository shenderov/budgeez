package com.budgeez.model.handlers;

import com.budgeez.model.entities.dao.Currency;
import com.budgeez.model.entities.dao.Language;
import com.budgeez.model.entities.external.ChartRequestWrapper;
import com.budgeez.model.entities.dao.ChartSelection;
import com.budgeez.model.entities.external.EVersion;
import com.budgeez.model.enumerations.ChartSelectionIdEnum;
import com.budgeez.model.entities.external.ChartWrapper;
import com.budgeez.model.exceptions.UnknownSelectionIdException;
import com.budgeez.model.exceptions.codes.EntitiesErrorCode;
import com.budgeez.model.interfaces.IExceptionMessagesHelper;
import com.budgeez.model.interfaces.IGeneralRequestHandler;
import com.budgeez.model.interfaces.IGeneralStatisticsHandler;
import com.budgeez.model.interfaces.ISystemHelper;
import com.budgeez.model.repository.ChartSelectionRepository;
import com.budgeez.model.repository.CurrencyRepository;
import com.budgeez.model.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneralRequestHandler implements IGeneralRequestHandler {

    @Autowired
    private ChartSelectionRepository chartSelectionRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private IGeneralStatisticsHandler generalStatisticsHandler;

    @Autowired
    private IExceptionMessagesHelper exceptionMessagesHelper;

    @Autowired
    private ISystemHelper systemHelper;

    private EVersion version = null;

    public static final ChartSelectionIdEnum DEFAULT_CHART_SELECTION = ChartSelectionIdEnum.CURRENT_MONTH_AVG;

    public ChartWrapper getGeneralDatatable(ChartRequestWrapper chartRequestWrapper) throws UnknownSelectionIdException {
        ChartSelection selection = chartSelectionRepository.findOne(chartRequestWrapper.getChartSelection().getSelectionId());
        chartRequestWrapper.setChartSelection(selection);
        ChartWrapper wrapper;
        switch (chartRequestWrapper.getChartSelection().getSelectionId()) {
            case CURRENT_MONTH_AVG:
                wrapper = generalStatisticsHandler.getCurrentMonthAverage(chartRequestWrapper);
                break;
            case PREV_MONTH_AVG:
                wrapper = generalStatisticsHandler.getNMonthAgoAverage(chartRequestWrapper, 1);
                break;
            case PREV_THREE_MONTHS_AVG:
                wrapper = generalStatisticsHandler.getNMonthAgoAverage(chartRequestWrapper, 3);
                break;
            case LAST_YEAR_AVG:
                wrapper = generalStatisticsHandler.getNMonthAgoAverage(chartRequestWrapper, 12);
                break;
            case LAST_THREE_MONTHS_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getLastNMonthsAverageDetailed(chartRequestWrapper, 3);
                break;
            case LAST_SIX_MONTHS_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getLastNMonthsAverageDetailed(chartRequestWrapper, 6);
                break;
            case LAST_YEAR_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getLastNMonthsAverageDetailed(chartRequestWrapper, 12);
                break;
            default:
                throw new UnknownSelectionIdException(EntitiesErrorCode.UNKNOWN_CHART_SELECTION_ID, exceptionMessagesHelper.getLocalizedMessage("error.chartselection.unknown"));
        }
        return wrapper;
    }

    public ChartWrapper getDefaultDataTable() throws UnknownSelectionIdException {
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(chartSelectionRepository.findOne(DEFAULT_CHART_SELECTION));
        return getGeneralDatatable(requestWrapper);
    }

    public EVersion getVersion() {
        if(version == null){
            version = systemHelper.getVersion();
        }
        return version;
    }
//
//    public void refreshCache() {
//        version = null;
//    }

    public List<Language> getLanguages() {
        return (List<Language>) languageRepository.findAll();
    }

    public List<Currency> getCurrencies() {
        return (List<Currency>) currencyRepository.findAll();
    }

    public List<ChartSelection> getGeneralChartSelectionsList() {
        return chartSelectionRepository.findAllGeneralSelections();
    }

    public List<ChartSelection> getUserChartSelectionsList() {
        return chartSelectionRepository.findAllUserSelections();
    }
}
