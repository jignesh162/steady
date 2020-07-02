package com.steady.nifty.strategy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.steady.nifty.strategy.entity.NiftyOHLC;
import com.steady.nifty.strategy.service.NiftyOHLCService;

@RestController
public class NiftyOHLCControllerImpl implements NiftyOHLCController {

    private final NiftyOHLCService niftyOHLCService;

    @Autowired
    public NiftyOHLCControllerImpl(NiftyOHLCService niftyOHLCService) {
        this.niftyOHLCService = niftyOHLCService;
    }

    @Override
    public Iterable<NiftyOHLC> getAll() {
        return niftyOHLCService.findAllByOrderByStartDate();
    }

    @Override
    public boolean calculateOHLCDataFromNifty() {
        return niftyOHLCService.calculateOHLCDataFromNifty();
    }

    @Override
    public boolean calculateWeeklyOHLCDataFromNifty() {
        return niftyOHLCService.calculateWeeklyOHLCDataFromNifty();
    }

    @Override
    public boolean calculate1MonthE2ExpiryOHLCDataFromNifty() {
        return niftyOHLCService.calculate1MonthE2ExpiryOHLCDataFromNifty();
    }

    @Override
    public boolean calculate2MonthE2ExpiryOHLCDataFromNifty() {
        return niftyOHLCService.calculate2MonthE2ExpiryOHLCDataFromNifty();
    }
}
