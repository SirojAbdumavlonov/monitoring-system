package com.example.monitoringsystem.tool;


import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static LocalDate getStartOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return convertDateToLocalDate(calendar.getTime());
    }

    public static LocalDate getEndOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return convertDateToLocalDate(calendar.getTime());
    }
    public static LocalDate getStartOfLastWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return convertDateToLocalDate(calendar.getTime());
    }

    public static LocalDate getEndOfLastWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return convertDateToLocalDate(calendar.getTime());
    }
    public static LocalDate getStartOfCurrentWeek() {
        LocalDate now = LocalDate.now();
        return now.with(DayOfWeek.SUNDAY);
    }

    public static LocalDate getEndOfCurrentWeek() {
        LocalDate now = LocalDate.now();
        return now.with(DayOfWeek.SATURDAY);
    }
    public static LocalDate getStartOfCurrentYear() {
        return LocalDate.now().withDayOfYear(1);
    }

    public static LocalDate getEndOfCurrentYear() {
        return LocalDate.now().withMonth(Month.DECEMBER.getValue()).withDayOfMonth(31);
    }
    public static LocalDate getStartOfMonth(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate getEndOfMonth(int year, int month) {
        return LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
    }
    public static int getMonthNumber(String monthName) {
        // Convert the month name to uppercase to handle case-insensitive matching
        monthName = monthName.toUpperCase(Locale.ENGLISH);

        try {
            // Attempt to parse the month name
            Month month = Month.valueOf(monthName);
            // Month enums are 1-based, so return the value directly
            return month.getValue();
        } catch (IllegalArgumentException e) {
            // Handle invalid month name
            throw new IllegalArgumentException("Invalid month name: " + monthName, e);
        }
    }
    private static LocalDate convertDateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public static LocalDate getStartOfCurrentMonth() {
        return LocalDate.now().withDayOfMonth(1);
    }

    public static LocalDate getEndOfCurrentMonth() {
        return LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
    }
    public static LocalDate getStartOfLastYear() {
        return LocalDate.now().minusYears(1).withDayOfYear(1);
    }

    public static LocalDate getEndOfLastYear() {
        return LocalDate.now().minusYears(1).withDayOfYear(LocalDate.now().minusYears(1).lengthOfYear());
    }
    public static LocalDate getStartOfLastNDays(int n) {
        return LocalDate.now().minusDays(n - 1); // Subtract one day to get the start of the range
    }

    public static LocalDate getEndOfLastNDays() {
        return LocalDate.now(); // End date is the current date
    }

}
