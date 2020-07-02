package com.steady.nifty.strategy.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

public final class ModelUtil {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    private static final DateTimeFormatter SDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter SDF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String COMMA = ",";
    private static final String DOT = ".";

    private ModelUtil() {
        // Private constructor. No instances needed.
    }

    public static final String convertToString(BigDecimal number) {
        return convertToString(number, 2);
    }

    public static final String convertToString(BigDecimal number, int precision) {
        if (number != null) {
            String format = "#0";
            if (precision > 0) {
                format += "." + StringUtils.repeat('0', precision);
            }
            DecimalFormat df = new DecimalFormat(format);
            DecimalFormatSymbols symbolSeparator = new DecimalFormatSymbols();
            symbolSeparator.setDecimalSeparator('.');
            df.setDecimalFormatSymbols(symbolSeparator);
            return df.format(number.setScale(precision, RoundingMode.HALF_UP));
        }
        return StringUtils.EMPTY;
    }

    public static final String convertDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.format(DTF);
        }
        return StringUtils.EMPTY;
    }

    public static final String convertDateTimeToShortString(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.format(SDTF);
        }
        return StringUtils.EMPTY;
    }

    public static final String convertDateTimeToDateString(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        return StringUtils.EMPTY;
    }

    public static final String convertDateToString(LocalDate localDate) {
        if (localDate != null) {
            return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        return StringUtils.EMPTY;
    }

    public static final String getCurrentDateAsString() {
        return LocalDate.now().format(SDF);
    }

    /**
     * Gets a BigDecimal from String and attempts to use either comma or dot as the decimal separator. Scale is not
     * modified.
     * 
     * @param value
     *            Number as string
     * @return String value as BigDecimal, null if unable to determine if comma or dot should be used as the decimal
     *         separator or if the String is not a number.
     */
    public static final BigDecimal getBigDecimal(String value) {
        return getBigDecimal(value, null);
    }

    /**
     * Gets a BigDecimal from String and Attempts to use either comma or dot as the decimal separator.
     * 
     * @param value
     *            Number as string
     * @param scale
     *            BigDecimal scale null = original scale
     * @return String value as BigDecimal, null if unable to determine if comma or dot should be used as the decimal
     *         separator or if the String is not a number.
     */
    public static final BigDecimal getBigDecimal(String value, Integer scale) {
        BigDecimal retVal = null;
        if (StringUtils.isBlank(value)) {
            return retVal;
        }
        String newValue = StringUtils.remove(value, StringUtils.SPACE);
        int commas = StringUtils.countMatches(newValue, COMMA);
        int dots = StringUtils.countMatches(newValue, DOT);
        if (commas > 1 && dots > 1 || !StringUtils.containsOnly(newValue, "-0123456789,.")
                || StringUtils.indexOf(newValue, '-') > 0) {
            // Can't figure out whether comma or dot is used as the decimal separator or
            // the string contains something else than just numbers.
            return retVal;
        }
        // Check if we have both commas and dots
        int lastComma = StringUtils.lastIndexOf(newValue, COMMA);
        int lastDot = StringUtils.lastIndexOf(newValue, DOT);
        if (lastComma >= 0 && lastDot >= 0) {
            // Remove commas or dots, depending which are first (we assume that a single decimal separator is the last).
            newValue = StringUtils.remove(newValue, lastComma > lastDot ? DOT : COMMA);
        } else if (lastComma >= 0 && commas > 1) {
            // Only commas (this is essentially integer)
            newValue = StringUtils.remove(newValue, COMMA);
        } else if (lastDot >= 0 && dots > 1) {
            // Only dots (this is essentially integer)
            newValue = StringUtils.remove(newValue, DOT);
        }

        // At this point we should have max single comma or dot as the decimal separator.
        // Normalize commas to dots.
        newValue = StringUtils.replace(newValue, COMMA, DOT);
        try {
            retVal = new BigDecimal(newValue);
            if (scale != null) {
                retVal = retVal.setScale(scale, RoundingMode.HALF_UP);
            }
        } catch (NumberFormatException nfe) {
            // Nothing to do
        }
        return retVal;
    }

    public static final Integer getInteger(String value) {
        Integer field = null;
        try {
            field = new Integer(value);
        } catch (NumberFormatException nfe) {
            // Nothing to do
        }
        return field;
    }

}
