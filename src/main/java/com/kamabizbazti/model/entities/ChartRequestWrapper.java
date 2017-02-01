package com.kamabizbazti.model.entities;

public class ChartRequestWrapper {

    private ChartSelection chartSelection;
    private DatePicker datePicker;

    public ChartSelection getChartSelection() {
        return chartSelection;
    }

    public void setChartSelection(ChartSelection chartSelection) {
        this.chartSelection = chartSelection;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    @Override
    public String toString() {
        return "ChartRequestWrapper{" +
                "chartSelection=" + chartSelection +
                ", datePicker=" + datePicker +
                '}';
    }
}
