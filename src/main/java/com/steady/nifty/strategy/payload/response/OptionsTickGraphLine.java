package com.steady.nifty.strategy.payload.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OptionsTickGraphLine {
    private String name;
    private List<GraphSeriesOnePoint> series = new ArrayList<>();

    public OptionsTickGraphLine(String name) {
        this.name = name;
    }
}
