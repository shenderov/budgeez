package com.kamabizbazti.model.entities.external;

public class DatePicker {

    private Long startDate;

    private Long endDate;

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DatePicker{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
