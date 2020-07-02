package com.steady.nifty.strategy.util;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

import static java.time.DayOfWeek.THURSDAY;
import static java.time.temporal.TemporalAdjusters.lastInMonth;

/**
 * This class is useful to convert option ticker in to object, so we can easily validate all the values in a single
 * place. NIFTY19DEC9500PE.NFO NIFTY 19 DEC 9500 PE/CE
 */

@Data
public class OptionsData implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;
    @NotBlank
    private Date expiryDate;
    @NotBlank
    private Integer strike;
    @NotBlank
    private String option;

    /**
     * Constructor to accept string id
     * @param data
     * 
     * @param id
     *               String id to be broken into multiple tokens.
     * @throws IOException
     */
    public OptionsData(@NotEmpty String ticker, String filePath) throws IOException {
        String optionString = StringUtils.split(ticker, ".")[0];
        if (optionString.length() < 16 || optionString.length() > 31) {
            System.out.println("ERROR: Incorrect ticker is - "+ticker+" and file path is - "+filePath+"\n");
        } else if(optionString.contains("_")) { //OPTIDX_NIFTY_27DEC2018_CE_10000
            String[] optionTokens = StringUtils.split(optionString, "_");
            this.name = optionTokens[1];
            this.expiryDate = getExpiryDateOfTheMonth(optionTokens[2].substring(2, 5), optionTokens[2].substring(5));
            this.option = optionTokens[3];
            this.strike = Integer.parseInt(optionTokens[4]);
        } else {
            try {
                this.name = optionString.substring(0, 5); // NIFTY
                this.expiryDate = getExpiryDateOfTheMonth(optionString.substring(7, 10), optionString.substring(5, 7));
                this.strike = Integer.parseInt(optionString.substring(10, optionString.length() - 2)); // 10900
                this.option = optionString.substring(optionString.length() - 2); // CE or PE
            } catch (NumberFormatException nfe) {
                System.out.println("ERROR: Incorrect strike price for the ticker - " + ticker+"\n");
            }
        }
        if (!"NIFTY".equals(name)) {
            System.out.println("ERROR: Incorrect option's name for the ticker - " + ticker+"\n");
        }
        if (!("CE".equals(option) || "PE".equals(option))) {
            System.out.println("ERROR: Incorrect option for the ticker - " + ticker+"\n");
        }
    }

    private Date getExpiryDateOfTheMonth(String month, String yearString) {
        int year = yearString.length() == 2 ? Integer.valueOf("20"+yearString) : Integer.valueOf(yearString);
        LocalDate lastThursday = LocalDate.of(year, Month.valueOf(month.toUpperCase()).getValue(), 1).with(lastInMonth(THURSDAY));
        return Date.valueOf(lastThursday.toString());
    }

    private enum Month {
        JAN,
        FEB,
        MAR,
        APR,
        MAY,
        JUN,
        JUL,
        AUG,
        SEP,
        OCT,
        NOV,
        DEC;

        public int getValue() {
            return ordinal() + 1;
        }
    } 
}
