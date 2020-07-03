package com.steady.nifty.strategy.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.steady.nifty.strategy.entity.Nifty;
import com.steady.nifty.strategy.entity.NiftyOHLC;
import com.steady.nifty.strategy.entity.WorkingDay;
import com.steady.nifty.strategy.repository.NiftyOHLCRepository;
import com.steady.nifty.strategy.repository.NiftyRepository;
import com.steady.nifty.strategy.repository.WorkingDayRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Validated
@Slf4j
public class NiftyOHLCServiceImpl implements NiftyOHLCService {

    private final NiftyOHLCRepository niftyOHLCRepository;
    private final NiftyRepository niftyRepository;
    private final WorkingDayRepository workingDayRepository;

    @Autowired
    public NiftyOHLCServiceImpl(NiftyOHLCRepository niftyOHLCRepository, NiftyRepository niftyRepository,
            WorkingDayRepository workingDayRepository) {
        this.niftyOHLCRepository = niftyOHLCRepository;
        this.niftyRepository = niftyRepository;
        this.workingDayRepository = workingDayRepository;
    }

    @Override
    public Iterable<NiftyOHLC> findAllByOrderByStartDate() {
        return niftyOHLCRepository.findAllByOrderByStartDate();
    }

    @Override
    public boolean calculateOHLCDataFromNifty() {
        List<NiftyOHLC> listToInsert = new ArrayList<>();
        Iterable<WorkingDay> workingDays = workingDayRepository.findAllByOrderByDay();

        for (WorkingDay workingDay : workingDays) {
            log.info("Working date: " + workingDay.getDay().toString());
            LocalDateTime startTime = LocalDateTime.of(workingDay.getDay(), LocalTime.parse("09:15:00"));
            LocalDateTime endTime = LocalDateTime.of(workingDay.getDay(), LocalTime.parse("15:30:00"));
            List<Nifty> niftyTicks = niftyRepository.findNiftyTicksForDay(startTime, endTime);
            if(niftyTicks.isEmpty()) {
                log.error("ERROR: Nifty value not found for the date : " + workingDay.getDay());
                continue;
            } else {
                NiftyOHLC niftyOHLC = calculateOHLC(workingDay.getDay(), workingDay.getDay(), workingDay.getIsExpiryDay(),
                    "D", niftyTicks);
                listToInsert.add(niftyOHLC);
            }
        }
        List<NiftyOHLC> savedNiftyOHLCList = niftyOHLCRepository.saveAll(listToInsert);
        return savedNiftyOHLCList.size() == listToInsert.size();
    }

    private NiftyOHLC calculateOHLC(LocalDate startDate, LocalDate endDate, Boolean isExpiryDay, String type,
            Iterable<Nifty> niftyTicks) {
        BigDecimal open = null;
        BigDecimal high = null;
        BigDecimal low = null;
        BigDecimal close = null;
        LocalDateTime highDateTime = null;
        LocalDateTime lowDateTime = null;

        try {
            boolean isFirstEntry = true;
            for (Nifty niftyTick : niftyTicks) {
                BigDecimal tradePrice = niftyTick.getValue();
                if(tradePrice == null) {
                    log.error("ERROR: Nifty value not found for the date : " + startDate);
                    continue;
                }
                if (isFirstEntry) {
                    open = high = low = close = tradePrice;
                    highDateTime = lowDateTime = niftyTick.getDateTime();
                    isFirstEntry = false;
                } else {
                    // Set highest price
                    if (high.subtract(tradePrice).intValue() < 0) {
                        high = tradePrice;
                        highDateTime = niftyTick.getDateTime();
                    }
                    // Set lowest price
                    if (low.subtract(tradePrice).intValue() > 0) {
                        low = tradePrice;
                        lowDateTime = niftyTick.getDateTime();
                    }
                    // Set close price
                    close = tradePrice;
                }
            }
        } catch (NullPointerException npe) {
            log.error("Failed for the date : " + startDate);
        }
        NiftyOHLC niftyOHLC = new NiftyOHLC();
        niftyOHLC.setOpen(open);
        niftyOHLC.setHigh(high);
        niftyOHLC.setLow(low);
        niftyOHLC.setClose(close);
        niftyOHLC.setHighTime(highDateTime);
        niftyOHLC.setLowTime(lowDateTime);
        niftyOHLC.setUp(high.subtract(open));
        niftyOHLC.setDown(open.subtract(low));
        niftyOHLC.setStartDate(startDate);
        niftyOHLC.setEndDate(endDate);
        niftyOHLC.setIsExpiryDay(isExpiryDay);
        niftyOHLC.setType(type);
        return niftyOHLC;
    }

    @Override
    public boolean calculateWeeklyOHLCDataFromNifty() {
        List<NiftyOHLC> listToInsert = new ArrayList<>();
        Iterable<WorkingDay> workingDays = workingDayRepository.findAllExpiryDaysOrderByDay();

        LocalDateTime startTime = LocalDateTime.of(LocalDate.parse("2018-01-01"), LocalTime.parse("09:15:00"));
        for (WorkingDay workingDay : workingDays) {
            LocalDateTime endTime = LocalDateTime.of(workingDay.getDay(), LocalTime.parse("15:30:00"));
            Iterable<Nifty> niftyTicks = niftyRepository.findNiftyTicksForDay(startTime, endTime);
            NiftyOHLC niftyOHLC = calculateOHLC(startTime.toLocalDate(), endTime.toLocalDate(),
                    workingDay.getIsExpiryDay(), "1W", niftyTicks);
            listToInsert.add(niftyOHLC);
            startTime = LocalDateTime.of(workingDay.getDay().plusDays(1), LocalTime.parse("09:15:00"));
        }
        List<NiftyOHLC> savedNiftyOHLCList = niftyOHLCRepository.saveAll(listToInsert);
        return savedNiftyOHLCList.size() == listToInsert.size();
    }

    @Override
    public boolean calculate1MonthE2ExpiryOHLCDataFromNifty() {
        List<NiftyOHLC> listToInsert = new ArrayList<>();
        Iterable<Date> monthExpiryDays = workingDayRepository.findMonthEndExpiryDaysGroupByMonth();

        LocalDateTime startTime = LocalDateTime.of(LocalDate.parse("2018-01-01"), LocalTime.parse("09:15:00"));
        for (Date monthExpiryDay : monthExpiryDays) {
            LocalDate date = monthExpiryDay.toLocalDate();
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.parse("15:30:00"));
            Iterable<Nifty> niftyTicks = niftyRepository.findNiftyTicksForDay(startTime, endTime);
            NiftyOHLC niftyOHLC = calculateOHLC(startTime.toLocalDate(), endTime.toLocalDate(), true, "1M", niftyTicks);
            listToInsert.add(niftyOHLC);
            startTime = LocalDateTime.of(date.plusDays(1), LocalTime.parse("09:15:00"));
        }
        List<NiftyOHLC> savedNiftyOHLCList = niftyOHLCRepository.saveAll(listToInsert);
        return savedNiftyOHLCList.size() == listToInsert.size();
    }

    @Override
    public boolean calculate2MonthE2ExpiryOHLCDataFromNifty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean calculate3MonthE2ExpiryOHLCDataFromNifty() {
        // TODO Auto-generated method stub
        return false;
    }
}
