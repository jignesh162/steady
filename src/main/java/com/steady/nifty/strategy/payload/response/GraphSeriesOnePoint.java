package com.steady.nifty.strategy.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GraphSeriesOnePoint {
    private String name;
    private String value;

    public GraphSeriesOnePoint(String name, String value) {
        this.name = name;
        this.value = value;
    }
}