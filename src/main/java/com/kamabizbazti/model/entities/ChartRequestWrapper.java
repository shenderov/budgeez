package com.kamabizbazti.model.entities;

public class ChartRequestWrapper {

    private ChartSelection chartSelection;
    private long startDate;
    private long endDate;

    public ChartSelection getChartSelection() {
        return chartSelection;
    }

    public void setChartSelection(ChartSelection chartSelection) {
        this.chartSelection = chartSelection;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
