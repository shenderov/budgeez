package com.kamabizbazti.model.interfaces;

public interface IDateHelper {
    long getFirstDayOfCurrentMonth();

    long getFirstDayOfPreviousMonth();

    long getFirstDayOfNMonthAgo(int monthAgo);

    long getFirsDayOfMonth(int monthAgo);

    long getLastDayOfMonth(int monthAgo);

    long getFirsDayOfWeek(int weeksAgo);

    long getLastDayOfWeek(int weeksAgo);

    long getLastDayOfCurrentMonth();

    long getLastDayOfPreviousMonth();

    long getLastDayOfNMonthAgo(int monthAgo);

    long weeksBetweenTwoDates(long startDate, long endDate);

    long monthsBetweenTwoDates(long startDate, long endDate);

    long yearsBetweenTwoDates(long startDate, long endDate);

    String getFullMonthName(long date);

    String getShortMonthName(long date);

    String getFullMonthWeek(long date);

    String getShortMonthWeek(long date);

    String getFullMonthYear(long date);

    String getShortMonthYear(long date);

    String getYear(long date);
}
