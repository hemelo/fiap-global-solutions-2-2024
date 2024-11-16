package org.global.console.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatUtils {

    private static final String DEFAULT_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DEFAULT_DATETIME_FORMAT);
    }

    public static String formatDateTime(LocalDateTime dateTime, String format) {

        if (dateTime == null) {
            return "";
        }

        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String formatDate(LocalDate date) {
        return formatDate(date, DEFAULT_DATE_FORMAT);
    }

    public static String formatDate(LocalDate date, String format) {

        if (date == null) {
            return "";
        }

        return date.format(DateTimeFormatter.ofPattern(format));
    }

}
