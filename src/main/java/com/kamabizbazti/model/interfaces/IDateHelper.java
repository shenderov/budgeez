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
    long weeksBetweenTwoDates(long startDate, long endDate);
    long monthssBetweenTwoDates(long startDate, long endDate);
    long yearsBetweenTwoDates(long startDate, long endDate);
}
