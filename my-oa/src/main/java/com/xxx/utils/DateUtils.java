package com.xxx.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateUtils {
    /**
     * 时间戳转换成日期
     * @param timestamp
     * @return
     */
    public static LocalDateTime timestamToDatetime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    /**
     * 日期转换为时间戳
     * @param timestamp
     * @return
     */
    public static long datatimeToTimestamp(LocalDateTime ldt) {
        long timestamp = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return timestamp;
    }
}
