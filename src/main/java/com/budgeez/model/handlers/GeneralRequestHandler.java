package com.budgeez.model.handlers;

import com.budgeez.model.entities.dao.Currency;
import com.budgeez.model.entities.dao.Language;
import com.budgeez.model.entities.external.ChartRequestWrapper;
import com.budgeez.model.entities.dao.ChartSelection;
import com.budgeez.model.entities.external.EVersion;
import com.budgeez.model.entities.internal.CacheWrapper;
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

    private List<Language> languages = null;

    private List<Currency> currencies = null;

    private List<ChartSelection> generalChartSelections = null;

    private List<ChartSelection> userChartSelections = null;

    private CacheWrapper defaultDataTable = null;

    private CacheWrapper currentMonthAvg = null;

    private CacheWrapper prevMonthAvg = null;

    private CacheWrapper prevThreeMonthsAvg = null;

    private CacheWrapper lastYearAvg = null;

    private CacheWrapper lastThreeMonthsAvgDetailed = null;

    private CacheWrapper lastSixMonthsAvgDetailed = null;

    private CacheWrapper lastYearAvgDetailed = null;

    private CacheWrapper versionCache = null;

    private static final long CHART_WRAPPERS_CACHE_EXPIRATION_TIME = 5 * 60 * 1000;

    private static final long VERSION_CACHE_EXPIRATION_TIME = 60 * 60 * 1000;

    public ChartWrapper getGeneralDatatable(ChartRequestWrapper chartRequestWrapper) throws UnknownSelectionIdException {
        ChartSelection selection = chartSelectionRepository.findOne(chartRequestWrapper.getChartSelection().getSelectionId());
        chartRequestWrapper.setChartSelection(selection);
        ChartWrapper wrapper;
        switch (chartRequestWrapper.getChartSelection().getSelectionId()) {
            case CURRENT_MONTH_AVG:
                wrapper = getCurrentMonthAvg(chartRequestWrapper);
                break;
            case PREV_MONTH_AVG:
                wrapper = getPrevMonthAvg(chartRequestWrapper);
                break;
            case PREV_THREE_MONTHS_AVG:
                wrapper = getPrevThreeMonthsAvg(chartRequestWrapper);
                break;
            case LAST_YEAR_AVG:
                wrapper = getLastYearAvg(chartRequestWrapper);
                break;
            case LAST_THREE_MONTHS_AVG_DETAILED:
                wrapper = getLastThreeMonthsAvgDetailed(chartRequestWrapper);
                break;
            case LAST_SIX_MONTHS_AVG_DETAILED:
                wrapper = getLastSixMonthsAvgDetailed(chartRequestWrapper);
                break;
            case LAST_YEAR_AVG_DETAILED:
                wrapper = getLastYearAvgDetailed(chartRequestWrapper);
                break;
            default:
                throw new UnknownSelectionIdException(EntitiesErrorCode.UNKNOWN_CHART_SELECTION_ID, exceptionMessagesHelper.getLocalizedMessage("error.chartselection.unknown"));
        }
        return wrapper;
    }

    private ChartWrapper getCurrentMonthAvg(ChartRequestWrapper chartRequestWrapper){
        if(currentMonthAvg != null && currentMonthAvg.getExpirationTime() <= System.currentTimeMillis()){
            currentMonthAvg = null;
        }
        if(currentMonthAvg == null){
            currentMonthAvg = new CacheWrapper(generalStatisticsHandler.getCurrentMonthAverage(chartRequestWrapper), CHART_WRAPPERS_CACHE_EXPIRATION_TIME);
        }
        return (ChartWrapper) currentMonthAvg.getObject();
    }

    private ChartWrapper getPrevMonthAvg(ChartRequestWrapper chartRequestWrapper){
        if(prevMonthAvg != null && prevMonthAvg.getExpirationTime() <= System.currentTimeMillis()){
            prevMonthAvg = null;
        }
        if(prevMonthAvg == null){
            prevMonthAvg = new CacheWrapper(generalStatisticsHandler.getNMonthAgoAverage(chartRequestWrapper, 1), CHART_WRAPPERS_CACHE_EXPIRATION_TIME);
        }
        return (ChartWrapper) prevMonthAvg.getObject();
    }

    private ChartWrapper getPrevThreeMonthsAvg(ChartRequestWrapper chartRequestWrapper){
        if(prevThreeMonthsAvg != null && prevThreeMonthsAvg.getExpirationTime() <= System.currentTimeMillis()){
            prevThreeMonthsAvg = null;
        }
        if(prevThreeMonthsAvg == null){
            prevThreeMonthsAvg = new CacheWrapper(generalStatisticsHandler.getNMonthAgoAverage(chartRequestWrapper, 3), CHART_WRAPPERS_CACHE_EXPIRATION_TIME);
        }
        return (ChartWrapper) prevThreeMonthsAvg.getObject();
    }

    private ChartWrapper getLastYearAvg(ChartRequestWrapper chartRequestWrapper){
        if(lastYearAvg != null && lastYearAvg.getExpirationTime() <= System.currentTimeMillis()){
            lastYearAvg = null;
        }
        if(lastYearAvg == null){
            lastYearAvg = new CacheWrapper(generalStatisticsHandler.getNMonthAgoAverage(chartRequestWrapper, 12), CHART_WRAPPERS_CACHE_EXPIRATION_TIME);
        }
        return (ChartWrapper) lastYearAvg.getObject();
    }

    private ChartWrapper getLastThreeMonthsAvgDetailed(ChartRequestWrapper chartRequestWrapper){
        if(lastThreeMonthsAvgDetailed != null && lastThreeMonthsAvgDetailed.getExpirationTime() <= System.currentTimeMillis()){
            lastThreeMonthsAvgDetailed = null;
        }
        if(lastThreeMonthsAvgDetailed == null){
            lastThreeMonthsAvgDetailed = new CacheWrapper(generalStatisticsHandler.getLastNMonthsAverageDetailed(chartRequestWrapper, 3), CHART_WRAPPERS_CACHE_EXPIRATION_TIME);
        }
        return (ChartWrapper) lastThreeMonthsAvgDetailed.getObject();
    }

    private ChartWrapper getLastSixMonthsAvgDetailed(ChartRequestWrapper chartRequestWrapper){
        if(lastSixMonthsAvgDetailed != null && lastSixMonthsAvgDetailed.getExpirationTime() <= System.currentTimeMillis()){
            lastSixMonthsAvgDetailed = null;
        }
        if(lastSixMonthsAvgDetailed == null){
            lastSixMonthsAvgDetailed = new CacheWrapper(generalStatisticsHandler.getLastNMonthsAverageDetailed(chartRequestWrapper, 6), CHART_WRAPPERS_CACHE_EXPIRATION_TIME);
        }
        return (ChartWrapper) lastSixMonthsAvgDetailed.getObject();
    }

    private ChartWrapper getLastYearAvgDetailed(ChartRequestWrapper chartRequestWrapper){
        if(lastYearAvgDetailed != null && lastYearAvgDetailed.getExpirationTime() <= System.currentTimeMillis()){
            lastYearAvgDetailed = null;
        }
        if(lastYearAvgDetailed == null){
            lastYearAvgDetailed = new CacheWrapper(generalStatisticsHandler.getLastNMonthsAverageDetailed(chartRequestWrapper, 12), CHART_WRAPPERS_CACHE_EXPIRATION_TIME);
        }
        return (ChartWrapper) lastYearAvgDetailed.getObject();
    }

    public ChartWrapper getDefaultDataTable() throws UnknownSelectionIdException {
        return (ChartWrapper) getDefaultDataTableCache().getObject();
    }

    private CacheWrapper getDefaultDataTableCache(){
        if(defaultDataTable != null && defaultDataTable.getExpirationTime() <= System.currentTimeMillis()){
            defaultDataTable = null;
        }
        if(defaultDataTable == null){
            ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
            requestWrapper.setChartSelection(chartSelectionRepository.findOne(ChartSelectionIdEnum.CURRENT_MONTH_AVG));
            ChartWrapper wrapper = generalStatisticsHandler.getCurrentMonthAverage(requestWrapper);
            Object [] dataTable = (Object[]) wrapper.getDataTable();
            if(dataTable.length == 1){
                wrapper = generalStatisticsHandler.getCurrentMonthAverageDummy(requestWrapper);
            }
            defaultDataTable = new CacheWrapper(wrapper, CHART_WRAPPERS_CACHE_EXPIRATION_TIME);
        }
        return defaultDataTable;
    }

    public EVersion getVersion() {
        if(versionCache != null && versionCache.getExpirationTime() <= System.currentTimeMillis()){
            versionCache = null;
        }
        if(versionCache == null){
            versionCache = new CacheWrapper(systemHelper.getVersion(), VERSION_CACHE_EXPIRATION_TIME);
        }
        return (EVersion) versionCache.getObject();
    }

    public List<Language> getLanguages() {
        if(languages == null){
            languages = (List<Language>) languageRepository.findAll();
        }
        return languages;
    }

    public void refreshLanguagesCache() {
        languages = null;
    }

    public List<Currency> getCurrencies() {
        if(currencies == null){
            currencies = (List<Currency>) currencyRepository.findAll();
        }
        return currencies;
    }

    public void refreshCurrenciesCache() {
        currencies = null;
    }

    public List<ChartSelection> getGeneralChartSelectionsList() {
        if(generalChartSelections == null){
            generalChartSelections = chartSelectionRepository.findAllGeneralSelections();
        }
        return generalChartSelections;
    }

    public void refreshGeneralChartSelectionsCache() {
        generalChartSelections = null;
    }

    public List<ChartSelection> getUserChartSelectionsList() {
        if(userChartSelections == null){
            userChartSelections = chartSelectionRepository.findAllUserSelections();
        }
        return userChartSelections;
    }

    public void refreshUserChartSelectionsCache() {
        userChartSelections = null;
    }


}
