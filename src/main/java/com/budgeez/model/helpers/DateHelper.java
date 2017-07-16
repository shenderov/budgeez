package com.budgeez.model.helpers;

import com.budgeez.model.interfaces.IDateHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class DateHelper implements IDateHelper {

    private static final SimpleDateFormat SDF_MONTH_FULL = new SimpleDateFormat("MMMM");
    private static final SimpleDateFormat SDF_MONTH_SHORT = new SimpleDateFormat("MMM");
    private static final SimpleDateFormat SDF_MONTH_FULL_WEEK = new SimpleDateFormat("MMMM'/W'W");
    private static final SimpleDateFormat SDF_MONTH_SHORT_WEEK = new SimpleDateFormat("MMM'/W'W");
    private static final SimpleDateFormat SDF_MONTH_FULL_WEEK_YEAR = new SimpleDateFormat("MMMM'/W'W YYYY");
    private static final SimpleDateFormat SDF_MONTH_SHORT_WEEK_YEAR = new SimpleDateFormat("MMM'/W'W YY");
    private static final SimpleDateFormat SDF_MONTH_FULL_YEAR = new SimpleDateFormat("MMMM YYYY");
    private static final SimpleDateFormat SDF_MONTH_SHORT_YEAR = new SimpleDateFormat("MMM YY");
    private static final SimpleDateFormat SDF_YEAR = new SimpleDateFormat("YYYY");


    public long getFirstDayOfCurrentMonth() {
        return getFirsDayOfMonth(0);
    }

    public long getFirstDayOfPreviousMonth() {
        return getFirsDayOfMonth(1);
    }

    public long getFirstDayOfNMonthAgo(int monthAgo) {
        return getFirsDayOfMonth(monthAgo);
    }

    public long getLastDayOfCurrentMonth() {
        return getLastDayOfMonth(0);
    }

    public long getLastDayOfPreviousMonth() {
        return getLastDayOfMonth(1);
    }

    public long getLastDayOfNMonthAgo(int monthAgo) {
        return getLastDayOfMonth(monthAgo);
    }

    public long getFirsDayOfMonth(int monthAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -monthAgo);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    public long getLastDayOfMonth(int monthAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -monthAgo);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTimeInMillis();
    }

    public long getFirsDayOfWeeksAgo(int weeksAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_YEAR, -weeksAgo);
        c.set(Calendar.DAY_OF_WEEK, c.getActualMinimum(Calendar.DAY_OF_WEEK));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    public long getLastDayOfWeeksAgo(int weeksAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_YEAR, -weeksAgo);
        c.set(Calendar.DAY_OF_WEEK, c.getActualMaximum(Calendar.DAY_OF_WEEK));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTimeInMillis();
    }

    public long getFirstDayOfWeekByDate(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.set(Calendar.DAY_OF_WEEK, c.getActualMinimum(Calendar.DAY_OF_WEEK));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    public long getFirstDayOfMonthByDate(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    public long getFirstDayOfYearByDate(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.set(Calendar.DAY_OF_YEAR, c.getActualMinimum(Calendar.DAY_OF_YEAR));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    public long getLastDayOfWeekByDate(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.set(Calendar.DAY_OF_WEEK, c.getActualMaximum(Calendar.DAY_OF_WEEK));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTimeInMillis();
    }

    public long getLastDayOfMonthByDate(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTimeInMillis();
    }

    public long getLastDayOfYearByDate(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTimeInMillis();
    }

    public long getFirstDayOfNWeeksAgoFromDate(long date, int weeksAgo) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.add(Calendar.WEEK_OF_YEAR, -weeksAgo);
        c.set(Calendar.DAY_OF_WEEK, c.getActualMinimum(Calendar.DAY_OF_WEEK));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    public long getFirstDayOfMonthAgoFromDate(long date, int monthsAgo) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.add(Calendar.MONTH, -monthsAgo);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    public long getFirstDayOfNYearsAgoFromDate(long date, int yearsAgo) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.add(Calendar.YEAR, -yearsAgo);
        c.set(Calendar.DAY_OF_YEAR, c.getActualMinimum(Calendar.DAY_OF_YEAR));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    public long weeksBetweenTwoDates(long startDate, long endDate) {
        Instant d1i = Instant.ofEpochMilli(startDate);
        Instant d2i = Instant.ofEpochMilli(endDate);
        LocalDateTime sDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
        LocalDateTime eDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());
        return ChronoUnit.WEEKS.between(sDate, eDate);
    }

    public long monthsBetweenTwoDates(long startDate, long endDate) {
        Instant d1i = Instant.ofEpochMilli(startDate);
        Instant d2i = Instant.ofEpochMilli(endDate);
        LocalDateTime sDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
        LocalDateTime eDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());
        return ChronoUnit.MONTHS.between(sDate, eDate);
    }

    public long yearsBetweenTwoDates(long startDate, long endDate) {
        Instant d1i = Instant.ofEpochMilli(startDate);
        Instant d2i = Instant.ofEpochMilli(endDate);
        LocalDateTime sDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
        LocalDateTime eDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());
        return ChronoUnit.YEARS.between(sDate, eDate);
    }

    public String getFullMonthName(long date) {
        return SDF_MONTH_FULL.format(date);
    }

    public String getShortMonthName(long date) {
        return SDF_MONTH_SHORT.format(date);
    }

    public String getFullMonthWeek(long date) {
        return SDF_MONTH_FULL_WEEK.format(date);
    }

    public String getShortMonthWeek(long date) {
        return SDF_MONTH_SHORT_WEEK.format(date);
    }

    public String getFullMonthWeekYear(long date) {
        return SDF_MONTH_FULL_WEEK_YEAR.format(date);
    }

    public String getShortMonthWeekYear(long date) {
        return SDF_MONTH_SHORT_WEEK_YEAR.format(date);
    }

    public String getYear(long date) {
        return SDF_YEAR.format(date);
    }

    public String getFullMonthYear(long date) {
        return SDF_MONTH_FULL_YEAR.format(date);
    }

    public String getShortMonthYear(long date) {
        return SDF_MONTH_SHORT_YEAR.format(date);
    }

    public String getFormattedMonthNameByDate(boolean isTheSameYear, long date, int monthsCount) {
        String month;
        if (isTheSameYear && (monthsCount > 6))
            month = getShortMonthName(date);
        else if (!isTheSameYear && (monthsCount <= 6))
            month = getFullMonthYear(date);
        else if (!isTheSameYear && (monthsCount > 6))
            month = getShortMonthYear(date);
        else
            month = getFullMonthName(date);
        return month;
    }

    public String getFormattedWeekNameByDate(boolean isTheSameYear, long date, int weeksCount) {
        String week;
        if (isTheSameYear && (weeksCount > 6))
            week = getShortMonthWeek(date);
        else if (!isTheSameYear && (weeksCount <= 6))
            week = getFullMonthWeekYear(date);
        else if (!isTheSameYear && (weeksCount > 6))
            week = getShortMonthWeekYear(date);
        else
            week = getFullMonthWeek(date);
        return week;
    }

}
