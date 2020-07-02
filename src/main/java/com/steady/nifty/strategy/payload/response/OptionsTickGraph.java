package com.steady.nifty.strategy.payload.response;

import java.util.List;

import lombok.Data;

@Data
public class OptionsTickGraph {
    List<OptionsTickGraphLine> optionsTickGraphLineList;
}