package com.budgeez.model.entities.dao;

import com.budgeez.model.enumerations.ChartSelectionIdEnum;
import com.budgeez.model.enumerations.ChartType;

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
    private Boolean datePicker;

    @Column(name = "authRequired", nullable = false)
    private Boolean authRequired;

    public ChartSelection() {
        super();
    }

    public ChartSelection(ChartSelectionIdEnum selectionId, String title, ChartType chartType, Boolean datePicker,
                          Boolean authRequired) {
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

    public Boolean isDatePicker() {
        return datePicker;
    }

    public void setDatePicker(Boolean datePicker) {
        this.datePicker = datePicker;
    }

    public Boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(Boolean authRequired) {
        this.authRequired = authRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChartSelection)) return false;

        ChartSelection selection = (ChartSelection) o;

        if (selectionId != selection.selectionId) return false;
        if (!title.equals(selection.title)) return false;
        if (chartType != selection.chartType) return false;
        if (!datePicker.equals(selection.datePicker)) return false;
        return authRequired.equals(selection.authRequired);
    }

    @Override
    public int hashCode() {
        int result = selectionId.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + chartType.hashCode();
        result = 31 * result + datePicker.hashCode();
        result = 31 * result + authRequired.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ChartSelection{" +
                "selectionId=" + selectionId +
                ", title='" + title + '\'' +
                ", chartType=" + chartType.toString() +
                ", datePicker=" + datePicker +
                ", authRequired=" + authRequired +
                '}';
    }
}
