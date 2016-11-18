package com.kamabizbazti.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "Selection", uniqueConstraints = @UniqueConstraint(columnNames = {"selectionId"}))
public class ChartSelection {

    @Id
    @Column(name = "selectionId", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChartSelectionId selectionId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "chartType", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChartType chartType;

    @Column(name = "datePicker", nullable = false)
    private boolean datePicker;

    @Column(name = "authRequired", nullable = false)
    @JsonIgnore
    private boolean authRequired;

    public ChartSelection() {
        super();
    }

    public ChartSelection(ChartSelectionId selectionId, String title, ChartType chartType, boolean datePicker,
                          boolean authRequired) {
        super();
        this.selectionId = selectionId;
        this.title = title;
        this.chartType = chartType;
        this.datePicker = datePicker;
        this.authRequired = authRequired;
    }

    public ChartSelectionId getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(ChartSelectionId selectionId) {
        this.selectionId = selectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
    }

    public boolean isDatePicker() {
        return datePicker;
    }

    public void setDatePicker(boolean datePicker) {
        this.datePicker = datePicker;
    }

    public boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }
}
