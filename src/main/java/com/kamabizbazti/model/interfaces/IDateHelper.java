package com.kamabizbazti.model.interfaces;

public interface IDateHelper {
    long getFirstDayOfCurrentMonth();

    long getFirstDayOfPreviousMonth();

    long getFirstDayOfNMonthAgo(int monthAgo);

    long getFirsDayOfMonth(int monthAgo);

    long getLastDayOfMonth(int monthAgo);

    long getFirsDayOfWeeksAgo(int weeksAgo);

    long getLastDayOfWeeksAgo(int weeksAgo);

    long getFirstDayOfNWeeksAgoFromDate(long date, int weeksAgo);

    long getFirstDayOfWeekByDate(long date);

    long getFirstDayOfMonthByDate(long date);

    long getFirstDayOfYearByDate(long date);

    long getLastDayOfWeekByDate(long date);

    long getLastDayOfMonthByDate(long date);

    long getLastDayOfYearByDate(long date);

    long getLastDayOfCurrentMonth();

    long getLastDayOfPreviousMonth();

    long getLastDayOfNMonthAgo(int monthAgo);

    long weeksBetweenTwoDates(long startDate, long endDate);

    long monthsBetweenTwoDates(long startDate, long endDate);

    long getFirstDayOfMonthAgoFromDate(long date, int monthsAgo);

    long getFirstDayOfNYearsAgoFromDate(long date, int yearsAgo);

    long yearsBetweenTwoDates(long startDate, long endDate);

    String getFullMonthName(long date);

    String getShortMonthName(long date);

    String getFullMonthWeek(long date);

    String getShortMonthWeek(long date);

    String getFullMonthWeekYear(long date);

    String getShortMonthWeekYear(long date);

    String getFullMonthYear(long date);

    String getShortMonthYear(long date);

    String getYear(long date);

    String getFormattedMonthNameByDate(boolean isTheSameYear, long date, int monthsCount);

    String getFormattedWeekNameByDate(boolean isTheSameYear, long date, int weeksCount);
}
