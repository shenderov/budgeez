package com.kamabizbazti.model.dao;

import com.kamabizbazti.model.enumerations.ChartSelectionIdEnum;
import com.kamabizbazti.model.enumerations.ChartType;

import javax.persistence.*;

@SuppressWarnings({"UnusedDeclaration"})
@Entity
@Table(name = "Selection", uniqueConstraints = @UniqueConstraint(columnNames = {"selectionId"}))
public class ChartSelection {

    @Id
    @Column(name = "selectionId", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChartSelectionIdEnum selectionId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "chartType", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChartType chartType;

    @Column(name = "datePicker", nullable = false)
    private boolean datePicker;

    @Column(name = "authRequired", nullable = false)
    private boolean authRequired;

    public ChartSelection() {
        super();
    }

    public ChartSelection(ChartSelectionIdEnum selectionId, String title, ChartType chartType, boolean datePicker,
                          boolean authRequired) {
        super();
        this.selectionId = selectionId;
        this.title = title;
        this.chartType = chartType;
        this.datePicker = datePicker;
        this.authRequired = authRequired;
    }

    public ChartSelectionIdEnum getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(ChartSelectionIdEnum selectionId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChartSelection)) return false;

        ChartSelection that = (ChartSelection) o;

        if (datePicker != that.datePicker) return false;
        if (authRequired != that.authRequired) return false;
        if (selectionId != that.selectionId) return false;
        if (!title.equals(that.title)) return false;
        return chartType == that.chartType;
    }

    @Override
    public int hashCode() {
        int result = selectionId.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + chartType.hashCode();
        result = 31 * result + (datePicker ? 1 : 0);
        result = 31 * result + (authRequired ? 1 : 0);
        return result;
    }
}
