package com.steady.nifty.strategy.payload.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.steady.nifty.strategy.util.EntityUtil;
import com.steady.nifty.strategy.util.ModelUtil;

import lombok.Data;

@Data
public class StrategyRequest {
    private LocalDateTime entryDateTime;
    private LocalDateTime exitDateTime;
    private Integer strikeAwayPoints;
    private String quantityOrPrice; //Same quantity or same price
    private BigDecimal initialAmount;
    private BigDecimal stopLossPercentage;
    private BigDecimal profitBookPercentage;

    @JsonSetter("entryDateTime")
    public void setEntryDateTimeString(String entryDateTime) {
        this.entryDateTime = EntityUtil.convertStringToLocalDateTime(entryDateTime);
    }

    @JsonSetter("exitDateTime")
    public void setExitDateTimeString(String exitDateTime) {
        this.exitDateTime = EntityUtil.convertStringToLocalDateTime(exitDateTime);
    }

    @JsonSetter("initialAmount")
    public void setInitialAmountString(String initialAmount) {
        this.initialAmount = ModelUtil.getBigDecimal(initialAmount, 2);
    }

    @JsonSetter("stopLossPercentage")
    public void setStopLossPercentageString(String stopLossPercentage) {
        this.stopLossPercentage = ModelUtil.getBigDecimal(stopLossPercentage, 2);
    }

    @JsonSetter("profitBookPercentage")
    public void setProfitBookPercentageString(String profitBookPercentage) {
        this.profitBookPercentage = ModelUtil.getBigDecimal(profitBookPercentage, 2);
    }
}
