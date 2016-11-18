package com.kamabizbazti.model.entities;

public class DatePicker {
    private long startDate;
    private long endDate;

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

    @Override
    public String toString() {
        return "DatePicker{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
