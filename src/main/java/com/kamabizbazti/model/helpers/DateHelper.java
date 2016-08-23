package com.kamabizbazti.model.helpers;

import com.kamabizbazti.model.interfaces.IDateHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class DateHelper implements IDateHelper{
private static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM");

    @Override
    public long getFirstDayOfCurrentMonth() {
        return getFirsDayOfMonth(0);
    }

    @Override
    public long getFirstDayOfPreviousMonth() {
        return getFirsDayOfMonth(1);
    }

    @Override
    public long getFirstDayOfNMonthAgo(int monthAgo) {
        return getFirsDayOfMonth(monthAgo);
    }

    @Override
    public long getLastDayOfCurrentMonth() {
        return getLastDayOfMonth(0);
    }

    @Override
    public long getLastDayOfPreviousMonth() {
        return getLastDayOfMonth(1);
    }

    @Override
    public long getLastDayOfNMonthAgo(int monthAgo) {
        return getLastDayOfMonth(monthAgo);
    }

    private long getFirsDayOfMonth(int monthAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, - monthAgo);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    private long getLastDayOfMonth(int monthAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, - monthAgo);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTimeInMillis();
    }

    public String getMonthNameByDate(long startDate) {
        return sdf.format(startDate);
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
}
