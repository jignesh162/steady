package com.steady.nifty.strategy.payload.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StrategyResponse {
    private String id;
    //UI data
    private String entryDateTime;
    private String exitDateTime;
    private BigDecimal initialAmount;
    private BigDecimal stopLossPercentage;
    private BigDecimal profitBookPercentage;
    private String quantityOrPrice;
    private Integer strikeAwayPoints;

    ///Data used for calculation
    private Integer strikeUsed;
    private String expiryDate;
    private Integer peOptionsId;
    private Integer ceOptionsId;
    /// PE
    private Integer peLotQty;
    private BigDecimal peBuyPrice;
    private BigDecimal peBuyPricePerLot;
    private BigDecimal peBuyAmount;
    /// CE
    private Integer ceLotQty;
    private BigDecimal ceBuyPrice;
    private BigDecimal ceBuyPricePerLot;
    private BigDecimal ceBuyAmount;

    //Result data
    private BigDecimal buyAmount;
    private BigDecimal buyPercentage;
    private String buyDateTime;

    private BigDecimal highestAmountGoesTo;
    private BigDecimal highestPercentage;
    private String highestAmountDateTime;

    private BigDecimal lowestAmountGoesTo;
    private BigDecimal lowestPercentage;
    private String lowestAmountDateTime;

    private BigDecimal sellAmount;
    private BigDecimal sellPercentage;
    private String sellDateTime;

    /// PE
    private BigDecimal peSellPrice;
    private BigDecimal peSellPricePerLot;
    private BigDecimal peSellAmount;
    /// CE
    private BigDecimal ceSellPrice;
    private BigDecimal ceSellPricePerLot;
    private BigDecimal ceSellAmount;

    private BigDecimal profitOrLossAmount;
    private BigDecimal profitOrLossPercentage;
    private String profitOrLossDateTime;

    private BigDecimal deInvestmentAmount;

    private BigDecimal balanceAmount;
    private OptionsTickGraph optionsTickGraph;
}
