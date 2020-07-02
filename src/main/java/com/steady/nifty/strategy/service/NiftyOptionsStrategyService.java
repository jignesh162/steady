package com.steady.nifty.strategy.service;

import com.steady.nifty.strategy.payload.request.StrategyRequest;
import com.steady.nifty.strategy.payload.response.StrategyResponse;

public interface NiftyOptionsStrategyService {
    public StrategyResponse runStratagegy(StrategyRequest strategyRequest);
}
