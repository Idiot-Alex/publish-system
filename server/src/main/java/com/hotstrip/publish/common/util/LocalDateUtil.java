package com.hotstrip.publish.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by idiot on 2019/3/14.
 */
public class LocalDateUtil {

    public static final String SDF_YM_PATTERN = "yyyy-MM";
    public static final String SIMPLE_DATE_HOURS_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String SDF_YMD_PATTERN = "yyyy-MM-dd";

    /**
     * LocalDateTime => String
     * @param localDateTime
     * @param format
     * @return
     */
    public static String getDateTimeAsString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    /**
     * String => LocalDateTime
     * @param time
     * @param format
     * @return
     */
    public static LocalDateTime parseStringToDateTime(String time, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(time, df);
    }

    /**
     * LocalDateTime => TimeStamp
     * @param localDateTime
     * @return
     */
    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * TimeStamp => LocalDateTime
     * @param timestamp
     * @return
     */
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }
}
