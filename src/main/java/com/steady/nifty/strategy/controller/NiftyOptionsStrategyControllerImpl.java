package com.steady.nifty.strategy.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.steady.nifty.strategy.payload.request.StrategyRequest;
import com.steady.nifty.strategy.payload.response.StrategyResponse;
import com.steady.nifty.strategy.service.NiftyOptionsStrategyService;

@RestController
public class NiftyOptionsStrategyControllerImpl implements NiftyOptionsStrategyController {

    private final NiftyOptionsStrategyService niftyOptionsStrategyService;

    @Autowired
    public NiftyOptionsStrategyControllerImpl(NiftyOptionsStrategyService niftyOptionsStrategyService) {
        this.niftyOptionsStrategyService = niftyOptionsStrategyService;
    }

    @Override
    public StrategyResponse runStratagegy(@Valid StrategyRequest strategyRequest) {
        return niftyOptionsStrategyService.runStratagegy(strategyRequest);
    }
}
