package com.kamabizbazti.model.interfaces;

/**
 * Created by macbookair13 on 20/08/2016.
 */
public interface IDateHelper {
    long getFirstDayOfCurrentMonth();
    long getFirstDayOfPreviousMonth();
    long getFirstDayOfNMonthAgo(int monthAgo);
    long getLastDayOfCurrentMonth();
    long getLastDayOfPreviousMonth();
    long getLastDayOfNMonthAgo(int monthAgo);
    String getMonthNameByDate(long startDate);
}
