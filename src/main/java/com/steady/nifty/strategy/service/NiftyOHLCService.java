package com.steady.nifty.strategy.service;

import com.steady.nifty.strategy.entity.NiftyOHLC;

public interface NiftyOHLCService {
    public Iterable<NiftyOHLC> findAllByOrderByStartDate();

    public boolean calculateOHLCDataFromNifty();

    public boolean calculateWeeklyOHLCDataFromNifty();

    public boolean calculate1MonthE2ExpiryOHLCDataFromNifty();

    public boolean calculate2MonthE2ExpiryOHLCDataFromNifty();

    public boolean calculate3MonthE2ExpiryOHLCDataFromNifty();
}
