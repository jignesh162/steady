package com.steady.nifty.strategy.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

public final class EntityUtil {

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private EntityUtil() {
        // Private constructor. No instances needed.
    }

    public static final LocalDate convertStringToLocalDate(String localDateString) {
        if (StringUtils.isNotEmpty(localDateString)) {
            return LocalDate.parse(localDateString, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        return null;
    }

    public static final LocalDateTime convertStringToLocalDateTime(String localDateTimeString) {
        if (StringUtils.isNotEmpty(localDateTimeString)) {
            return LocalDateTime
            .from(dateTimeFormatter.parse(localDateTimeString));
        }
        return null;
    }
}
