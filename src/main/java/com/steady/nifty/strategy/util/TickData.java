package com.steady.nifty.strategy.util;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * This class is useful to convert one tick in to object, so we can easily validate all the values in a single place.
 * Ticker,Date,Time,LTP,BuyPrice,BuyQty,SellPrice,SellQty,LTQ,Open Interest
 */

@Data
public class TickData implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String ticker;
    @NotNull
    private Timestamp dateTimestamp;
    @NotNull
    private String date;
    @NotNull
    private String time;
    @NotNull
    private BigDecimal ltp;
    @NotNull
    private BigDecimal buyPrice;
    @NotNull
    private Integer buyQty;
    @NotNull
    private BigDecimal sellPrice;
    @NotNull
    private Integer sellQty;
    @NotNull
    private Integer ltq;
    @NotNull
    private Integer openInterest;

    /**
     * Constructor to accept string oneRow
     * 
     * @param oneRow
     *                   String one row which contains Ticker,Date,Time,LTP,BuyPrice,BuyQty,SellPrice,SellQty,LTQ,Open
     *                   Interest
     * @throws IOException
     */
    public TickData(@NotEmpty String oneRow) throws IOException {
        String[] tickData = StringUtils.split(oneRow, ",");
        if (tickData.length != 10) {
            throw new IllegalArgumentException(
                    "ERROR: Incorrect tick data. Please confirm format like 'Ticker,Date,Time,LTP,BuyPrice,BuyQty,SellPrice,SellQty,LTQ,Open Interest'");
        }
        try {
            this.ticker = tickData[0];
            String[] dateTokens = tickData[1].split("/");
            this.dateTimestamp = Timestamp
                    .valueOf(dateTokens[2] + "-" + dateTokens[1] + "-" + dateTokens[0] + " " + tickData[2]);
            this.date = dateTokens[2] + "-" + dateTokens[1] + "-" + dateTokens[0];
            this.time = tickData[2];
            this.ltp = new BigDecimal(tickData[3]);
            this.buyPrice = new BigDecimal(tickData[4]);
            this.buyQty = Integer.parseInt(tickData[5]);
            this.sellPrice = new BigDecimal(tickData[6]);
            this.sellQty = Integer.parseInt(tickData[7]);
            this.ltq = Integer.parseInt(tickData[8]);
            this.openInterest = Integer.parseInt(tickData[9]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
            System.out.println("ERROR: Incorrect strike price for the ticker - " + ticker + " ERROR: " + exception.getMessage()+"\n");
        }
    }
}
