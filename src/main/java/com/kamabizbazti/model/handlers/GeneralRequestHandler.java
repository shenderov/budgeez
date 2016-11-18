package com.kamabizbazti.model.handlers;

import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.ChartSelectionId;
import com.kamabizbazti.model.entities.ChartWrapper;
import com.kamabizbazti.model.interfaces.IGeneralRequestHandler;
import com.kamabizbazti.model.interfaces.IGeneralStatisticsHandler;
import com.kamabizbazti.model.repository.ChartSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneralRequestHandler implements IGeneralRequestHandler {

    @Autowired
    private ChartSelectionRepository chartSelectionRepository;

    @Autowired
    private IGeneralStatisticsHandler generalStatisticsHandler;

    private static final ChartSelectionId DEFAULT_CHART_SELECTION = ChartSelectionId.CURRENT_MONTH_AVG;

    public ChartWrapper getGeneralDatatable(ChartRequestWrapper chartRequestWrapper) {
        ChartWrapper wrapper = null;
        switch (chartRequestWrapper.getChartSelection().getSelectionId()) {
            case CURRENT_MONTH_AVG:
            default:
                wrapper = generalStatisticsHandler.getCurrentMonthAverage(chartRequestWrapper);
                break;
            case PREV_MONTH_AVG:
                wrapper = generalStatisticsHandler.getNMonthAgoAverage(chartRequestWrapper, 1);
                break;
            case PREV_THREE_MONTH_AVG:
                wrapper = generalStatisticsHandler.getNMonthAgoAverage(chartRequestWrapper, 3);
                break;
            case CUSTOM_PERIOD_AVG:
                wrapper = generalStatisticsHandler.getCustomPeriodAverage(chartRequestWrapper);
                break;
            case LAST_THREE_MONTH_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getLastNMonthsAverageDetailed(chartRequestWrapper, 3);
                break;
            case LAST_SIX_MONTH_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getLastNMonthsAverageDetailed(chartRequestWrapper, 6);
                break;
            case LAST_YEAR_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getLastNMonthsAverageDetailed(chartRequestWrapper, 12);
                break;
            case CUSTOM_PERIOD_AVG_DETAILED:
                wrapper = generalStatisticsHandler.getGeneralCustomPeriodAverageDetailed(chartRequestWrapper);
                break;
        }
        return wrapper;
    }

    public ChartWrapper getDefaultDataTable() {
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(chartSelectionRepository.findOne(DEFAULT_CHART_SELECTION));
        return getGeneralDatatable(requestWrapper);
    }

    public List<ChartSelection> getGeneralChartSelectionsList() {
        return chartSelectionRepository.findAllGeneralSelections();
    }

    public List<ChartSelection> getUserChartSelectionsList() {
        return chartSelectionRepository.findAllUserSelections();
    }
}
