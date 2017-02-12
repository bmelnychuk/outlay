package app.outlay.core.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bogdan Melnychuk on 1/29/16.
 */
public final class DateUtils {

    private static final SimpleDateFormat SHORT_STRING_FORMAT = new SimpleDateFormat("dd MMM yy");
    private static final SimpleDateFormat YEAR_MONTH_STRING_FORMAT = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat LONG_STRING_FORMAT = new SimpleDateFormat("dd MMMM yyyy");

    public static String toYearMonthString(Date date) {
        return YEAR_MONTH_STRING_FORMAT.format(date);
    }

    public static String toShortString(Date date) {
        if (isToday(date)) {
            return "Today";
        }
        return SHORT_STRING_FORMAT.format(date);
    }

    public static String toLongString(Date date) {
        if (isToday(date)) {
            return "Today";
        }
        return LONG_STRING_FORMAT.format(date);
    }

    public static Date fillCurrentTime(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        dateTime = dateTime.withTime(LocalTime.now());
        return dateTime.toDate();
    }

    public static boolean isInPeriod(Date date, Date start, Date end) {
        DateTime dateTime = new DateTime(date);
        DateTime startDate = new DateTime(start);
        DateTime endDate = new DateTime(end);

        return dateTime.isAfter(startDate) && dateTime.isBefore(endDate);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    public static Date getWeekStart(Date date) {
        Date result = LocalDate.fromDateFields(date).withDayOfWeek(DateTimeConstants.MONDAY).toDate();
        return getDayStart(result);
    }

    public static Date getWeekEnd(Date date) {
        Date result = LocalDate.fromDateFields(date).withDayOfWeek(DateTimeConstants.SUNDAY).toDate();
        return getDayEnd(result);
    }

    public static Date getMonthStart(Date date) {
        Date result = LocalDate.fromDateFields(date).dayOfMonth().withMinimumValue().toDate();
        return getDayStart(result);
    }

    public static Date getMonthEnd(Date date) {
        Date result = LocalDate.fromDateFields(date).dayOfMonth().withMaximumValue().toDate();
        return getDayEnd(result);
    }

    public static Date getDayStart(Date date) {
        return LocalDate.fromDateFields(date).toDateTimeAtStartOfDay().toDate();
    }

    public static Date getDayEnd(Date date) {
        LocalTime time = new LocalTime().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
        return LocalDate.fromDateFields(date).toDateTime(time).toDate();
    }

    public static Date getMin(Date date1, Date date2) {
        return new DateTime(date1).isAfter(new DateTime(date2)) ? date2 : date1;
    }

    public static Date getMax(Date date1, Date date2) {
        return new DateTime(date1).isAfter(new DateTime(date2)) ? date1 : date2;
    }
}
