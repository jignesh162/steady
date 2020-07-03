package com.steady.nifty.strategy.service;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.steady.nifty.strategy.entity.Nifty;
import com.steady.nifty.strategy.entity.OptionsTick;
import com.steady.nifty.strategy.payload.request.StrategyRequest;
import com.steady.nifty.strategy.payload.response.GraphSeriesOnePoint;
import com.steady.nifty.strategy.payload.response.OptionsTickGraph;
import com.steady.nifty.strategy.payload.response.OptionsTickGraphLine;
import com.steady.nifty.strategy.payload.response.StrategyResponse;
import com.steady.nifty.strategy.repository.NiftyOptionsDetailsRepository;
import com.steady.nifty.strategy.repository.NiftyOptionsTickRepository;
import com.steady.nifty.strategy.repository.NiftyRepository;
import com.steady.nifty.strategy.repository.WorkingDayRepository;
import com.steady.nifty.strategy.util.ModelUtil;
import com.steady.nifty.strategy.util.Translator;

@Service
@Transactional
@Validated
public class NiftyOptionsStrategyServiceImpl implements NiftyOptionsStrategyService {

    private final NiftyOptionsTickRepository niftyOptionsTickRepository;
    private final NiftyOptionsDetailsRepository niftyOptionsDetailsRepository;
    private final NiftyRepository niftyRepository;
    private final WorkingDayRepository workingDayRepository;

    @Autowired
    public NiftyOptionsStrategyServiceImpl(NiftyOptionsTickRepository niftyOptionsTickRepository,
            NiftyOptionsDetailsRepository niftyOptionsDetailsRepository, NiftyRepository niftyRepository,
            WorkingDayRepository workingDayRepository) {
        this.niftyOptionsTickRepository = niftyOptionsTickRepository;
        this.niftyOptionsDetailsRepository = niftyOptionsDetailsRepository;
        this.niftyRepository = niftyRepository;
        this.workingDayRepository = workingDayRepository;
    }

    @Override
    public StrategyResponse runStratagegy(StrategyRequest strategyRequest) {
        StrategyResponse response = getStrategyResponse(strategyRequest);
        FileWriter logs = null;
        try {
            logs = new FileWriter("C://Learn//StrategyLog.txt");

            Integer strikeAwayPoints = strategyRequest.getStrikeAwayPoints();
            LocalDateTime entryDateTime = strategyRequest.getEntryDateTime();
            LocalDateTime exitDateTime = strategyRequest.getExitDateTime();
            BigDecimal initialAmount = strategyRequest.getInitialAmount();
            String quantityOrPrice = strategyRequest.getQuantityOrPrice();
            BigDecimal profitBookPercentage = strategyRequest.getProfitBookPercentage();
            BigDecimal stopLossPercentage = strategyRequest.getStopLossPercentage();

            // Get nifty value at entry time
            List<Nifty> niftyValues = niftyRepository.findNiftyTicksForDay(entryDateTime, exitDateTime);

            if (niftyValues.isEmpty()) {
                throw new EntityNotFoundException(
                        Translator.toLocale("entityNotFoundExceptionMessage", entryDateTime.toString()));
            }

            // Logic to decide entry point for the nifty
            LocalDateTime niftyEntryTime = null;
            BigDecimal niftyEntryValue = null;

            for (Nifty nifty : niftyValues) {
                if (niftyEntryValue == null) {
                    niftyEntryTime = nifty.getDateTime();
                    niftyEntryValue = nifty.getValue();
                    break;
                }
            }
            niftyEntryValue = niftyEntryValue.setScale(0, RoundingMode.UP);
            logs.append("Nifty value is " + niftyEntryValue + " at " + niftyEntryTime + "\n");

            Integer strike = Math.round(niftyEntryValue.floatValue() / 50f) * 50;
            response.setStrikeUsed(strike);

            logs.append("Found strike price :: " + strike + "\n");

            Integer callStrikePrice = strike + strikeAwayPoints;
            Integer putStrikePrice = strike - strikeAwayPoints;

            logs.append("callStrikePrice:: " + callStrikePrice + "\n");
            logs.append("putStrikePrice:: " + putStrikePrice + "\n");

            // Get expiry date for the month from working_day entity
            LocalDate expiryDate = workingDayRepository.findMonthEndExpiryByMonthAndYear(exitDateTime.getMonthValue(),
                    exitDateTime.getYear());
            response.setExpiryDate(expiryDate.toString());
            logs.append("Expiry date :: " + expiryDate + "\n");
            Integer ceId = niftyOptionsDetailsRepository
                    .findByExpiryDateAndStrikeAndOption(expiryDate, callStrikePrice, "CE").getId();
            response.setCeOptionsId(ceId);
            Integer peId = niftyOptionsDetailsRepository
                    .findByExpiryDateAndStrikeAndOption(expiryDate, putStrikePrice, "PE").getId();
            response.setPeOptionsId(peId);

            logs.append("CE Found :: " + ceId + "\n");
            logs.append("PE Found :: " + peId + "\n");
            Map<LocalDateTime, OptionsTick> ceOptionsTickMap = new HashMap<>();
            List<OptionsTick> optionsTickCE = niftyOptionsTickRepository.findByOptionsId(niftyEntryTime, exitDateTime,
                    ceId);
            for (OptionsTick optionsTick : optionsTickCE) {
                // logs.append("dateTime: "+optionsTick.getDateTime().toString()
                // +" ltp: " + optionsTick.getLtp() + "\n");
                ceOptionsTickMap.put(optionsTick.getDateTime(), optionsTick);

                if (niftyEntryTime.isEqual(optionsTick.getDateTime())) {
                    logs.append("CE same tick data found :: " + optionsTick.getDateTime().toString() + "\n");
                }
            }

            Map<LocalDateTime, OptionsTick> peOptionsTickMap = new HashMap<>();
            List<OptionsTick> optionsTickPE = niftyOptionsTickRepository.findByOptionsId(niftyEntryTime, exitDateTime,
                    peId);
            for (OptionsTick optionsTick : optionsTickPE) {
                // logs.append("dateTime: "+optionsTick.getDateTime().toString()
                // +" ltp: " + optionsTick.getLtp() + "\n");
                peOptionsTickMap.put(optionsTick.getDateTime(), optionsTick);
                if (niftyEntryTime.isEqual(optionsTick.getDateTime())) {
                    logs.append("PE same tick data found :: " + optionsTick.getDateTime().toString() + "\n");
                }
            }

            // Decide how much quantity you can buy
            BigDecimal cePrice = getNotNullTickData(new OptionsTick(), niftyEntryTime, ceOptionsTickMap, "CE", logs)
                    .getLtp();
            OptionsTick peOptionsTick = getNotNullTickData(new OptionsTick(), niftyEntryTime, peOptionsTickMap, "PE",
                    logs);
            BigDecimal pePrice = peOptionsTick.getLtp();

            BigDecimal cePerLotPrice = cePrice.multiply(BigDecimal.valueOf(75));
            BigDecimal pePerLotPrice = pePrice.multiply(BigDecimal.valueOf(75));

            Integer ceLots = 0;
            Integer peLots = 0;
            if ("quantity".equalsIgnoreCase(quantityOrPrice)) {
                // Same quantity of PE and CE
                logs.append("Same quantity for CE and PE...... \n");

                BigDecimal combinationLotPrice = cePerLotPrice.add(pePerLotPrice);
                Integer numberOfLotCanBuy = initialAmount.divideToIntegralValue(combinationLotPrice).intValue();

                ceLots = numberOfLotCanBuy;
                peLots = numberOfLotCanBuy;

                logs.append("CE number of lots : " + ceLots + " at price : " + cePerLotPrice + " total : "
                        + cePerLotPrice.multiply(BigDecimal.valueOf(ceLots)) + "\n");
                logs.append("PE number of lots : " + peLots + " at price : " + pePerLotPrice + " total : "
                        + pePerLotPrice.multiply(BigDecimal.valueOf(peLots)) + "\n");

            } else {
                // Same amount for PE and CE
                logs.append("Same amount for Ce and PE......\n");

                BigDecimal sameAmountAvailableForPEOrCE = initialAmount.divide(BigDecimal.valueOf(2), RoundingMode.UP);
                ceLots = sameAmountAvailableForPEOrCE.divideToIntegralValue(cePerLotPrice).intValue();
                peLots = sameAmountAvailableForPEOrCE.divideToIntegralValue(pePerLotPrice).intValue();
                logs.append("CE number of lots : " + ceLots + " at price : " + cePerLotPrice + " total : "
                        + cePerLotPrice.multiply(BigDecimal.valueOf(ceLots)) + "\n");
                logs.append("PE number of lots : " + peLots + " at price : " + pePerLotPrice + " total : "
                        + pePerLotPrice.multiply(BigDecimal.valueOf(peLots)) + "\n");
            }

            // Price reading
            BigDecimal peInvestmentAmount = pePerLotPrice.multiply(BigDecimal.valueOf(peLots));
            BigDecimal ceInvestmentAmount = cePerLotPrice.multiply(BigDecimal.valueOf(ceLots));
            BigDecimal totalInvestment = ceInvestmentAmount.add(peInvestmentAmount);

            response.setPeLotQty(peLots);
            response.setPeBuyPrice(pePrice);
            response.setPeBuyPricePerLot(pePerLotPrice);
            response.setPeBuyAmount(peInvestmentAmount);

            response.setCeLotQty(ceLots);
            response.setCeBuyPrice(cePrice);
            response.setCeBuyPricePerLot(cePerLotPrice);
            response.setCeBuyAmount(ceInvestmentAmount);

            response.setBuyAmount(totalInvestment);
            response.setBuyPercentage(
                    totalInvestment.multiply(new BigDecimal(100)).divide(initialAmount, RoundingMode.HALF_UP));
            response.setBuyDateTime(peOptionsTick.getDateTime().toString());
            logs.append("totalInvestment : " + totalInvestment + "\n");

            BigDecimal maxTotal = BigDecimal.ZERO;
            BigDecimal minTotal = BigDecimal.ZERO;
            boolean isFirstEntry = true;
            LocalDateTime maxValueTime = niftyEntryTime;
            LocalDateTime minValueTime = niftyEntryTime;
            OptionsTick ceLastTradedTick = null;
            OptionsTick peLastTradedTick = null;

            boolean isProfitBooked = false;
            boolean isStopLossHit = false;
            BigDecimal currentTotalValue = BigDecimal.ZERO;
            BigDecimal ceCurrentPrice = BigDecimal.ZERO;
            BigDecimal peCurrentPrice = BigDecimal.ZERO;

            BigDecimal cePerLotCurrentPrice = BigDecimal.ZERO;
            BigDecimal pePerLotCurrentPrice = BigDecimal.ZERO;

            BigDecimal ceCurrentTotalValue = BigDecimal.ZERO;
            BigDecimal peCurrentTotalValue = BigDecimal.ZERO;

            List<OptionsTickGraphLine> optionsTickGraphLineList = new ArrayList<>();

            OptionsTickGraphLine ceGraphLine = new OptionsTickGraphLine("CE_" + strike + "_" + expiryDate);
            OptionsTickGraphLine peGraphLine = new OptionsTickGraphLine("PE_" + strike + "_" + expiryDate);

            int counter = 0;

            long secondsBetween = ChronoUnit.SECONDS.between(entryDateTime, exitDateTime);
            long daysBetween = ChronoUnit.DAYS.between(entryDateTime, exitDateTime) == 0 ? 1
                    : ChronoUnit.DAYS.between(entryDateTime, exitDateTime);
            long modulo = 60 * daysBetween;
            long moduloSeconds = secondsBetween / 22500;
            System.out.println("secondsBetween: " + secondsBetween + "  daysBetween: " + daysBetween + "  modulo: "
                    + modulo + "  moduloSeconds: " + moduloSeconds);

            boolean matchCEAndPEPrices = ceOptionsTickMap.keySet().retainAll(peOptionsTickMap.keySet());
            System.out.println("matchCEAndPEPrices" + matchCEAndPEPrices);

            for (Entry<LocalDateTime, OptionsTick> ceOptionsTick : ceOptionsTickMap.entrySet()) {
                ceLastTradedTick = ceOptionsTick.getValue();
                ceCurrentPrice = ceLastTradedTick.getLtp();

                peLastTradedTick = peOptionsTickMap.get(ceOptionsTick.getKey());
                peCurrentPrice = peLastTradedTick.getLtp();

                //Date times is same for pe and ce????
                if(counter >= moduloSeconds) {
                    ceGraphLine.getSeries().add(new GraphSeriesOnePoint(ceOptionsTick.getKey().toString(), ceCurrentPrice.toString()));
                    peGraphLine.getSeries().add(new GraphSeriesOnePoint(ceOptionsTick.getKey().toString(), peCurrentPrice.toString()));
                    counter = 0;
                } else {
                    counter = counter + 1;
                }

                cePerLotCurrentPrice = ceCurrentPrice.multiply(BigDecimal.valueOf(75));
                pePerLotCurrentPrice = peCurrentPrice.multiply(BigDecimal.valueOf(75));

                ceCurrentTotalValue = cePerLotCurrentPrice.multiply(BigDecimal.valueOf(ceLots));
                peCurrentTotalValue = pePerLotCurrentPrice.multiply(BigDecimal.valueOf(peLots));

                currentTotalValue = ceCurrentTotalValue.add(peCurrentTotalValue);

                if(isFirstEntry) {
                    minTotal = maxTotal = currentTotalValue;
                    maxValueTime = minValueTime = ceOptionsTick.getKey();
                    isFirstEntry = false; 
                } else {
                    if ( currentTotalValue.subtract(maxTotal).doubleValue() > 0 ) {
                        maxTotal = currentTotalValue;
                        maxValueTime = ceOptionsTick.getKey();
                    }
                    if ( currentTotalValue.subtract(minTotal).doubleValue() <= 0 ) {
                        minTotal = currentTotalValue;
                        minValueTime = ceOptionsTick.getKey();
                    }
                }

                BigDecimal minusAmount = totalInvestment.subtract(minTotal);
                BigDecimal plusAmount = maxTotal.subtract(totalInvestment);

                BigDecimal upPercentage =  plusAmount.multiply(new BigDecimal(100)).divide(totalInvestment, RoundingMode.HALF_UP);
                BigDecimal downPercentage =  minusAmount.multiply(new BigDecimal(100)).divide(totalInvestment, RoundingMode.HALF_UP);

                if(checkProfitBook(profitBookPercentage, isProfitBooked, isStopLossHit, upPercentage)) {
                    logs.append("Profit booked " + upPercentage + "% and amount is : " + maxTotal + " at " + maxValueTime + "\n");
                    response.setSellDateTime(maxValueTime.toString());
                    response.setSellPercentage(upPercentage);
                    response.setSellAmount(currentTotalValue.subtract(totalInvestment));

                    response.setPeSellPrice(peCurrentPrice);
                    response.setPeSellPricePerLot(pePerLotCurrentPrice);
                    response.setPeSellAmount(peCurrentTotalValue);

                    response.setCeSellPrice(ceCurrentPrice);
                    response.setCeSellPricePerLot(cePerLotCurrentPrice);
                    response.setCeSellAmount(ceCurrentTotalValue);
                    
                    response.setProfitOrLossAmount(plusAmount);
                    response.setProfitOrLossPercentage(upPercentage);
                    response.setProfitOrLossDateTime(ceOptionsTick.getKey().toString());

                    response.setDeInvestmentAmount(maxTotal);
                    response.setBalanceAmount(initialAmount.add(plusAmount));
                    isProfitBooked = true;
                }

                if(checkStopLoss(stopLossPercentage, isProfitBooked, isStopLossHit, downPercentage)) {
                    logs.append("Stoploss hit " + downPercentage + "% and amount is : " + minTotal + " at " + minValueTime + "\n");
                    response.setSellDateTime(minValueTime.toString());
                    response.setSellPercentage(downPercentage);
                    response.setSellAmount(currentTotalValue.subtract(totalInvestment));

                    response.setPeSellPrice(peCurrentPrice);
                    response.setPeSellPricePerLot(pePerLotCurrentPrice);
                    response.setPeSellAmount(peCurrentTotalValue);

                    response.setCeSellPrice(ceCurrentPrice);
                    response.setCeSellPricePerLot(cePerLotCurrentPrice);
                    response.setCeSellAmount(ceCurrentTotalValue);
                    
                    response.setProfitOrLossAmount(minusAmount.negate());
                    response.setProfitOrLossPercentage(downPercentage);
                    response.setProfitOrLossDateTime(ceOptionsTick.getKey().toString());

                    response.setDeInvestmentAmount(minTotal);
                    response.setBalanceAmount(initialAmount.subtract(minusAmount));

                    isStopLossHit = true;
                }
            }
            logs.append("maxTotal : " + maxTotal  + " maxValueTime : " + maxValueTime  + "\n");
            logs.append("minTotal : " + minTotal  + " minValueTime : " + minValueTime  + "\n");

            BigDecimal minusAmount = totalInvestment.subtract(minTotal);
            BigDecimal plusAmount = maxTotal.subtract(totalInvestment);

            logs.append("UP : " +  plusAmount + " Percentage : " + plusAmount.multiply(new BigDecimal(100)).divide(totalInvestment, RoundingMode.HALF_UP) + "\n");
            logs.append("Down : " +  minusAmount + " Percentage : " + minusAmount.multiply(new BigDecimal(100)).divide(totalInvestment, RoundingMode.HALF_UP) + "\n");
            response.setLowestAmountGoesTo(minusAmount);
            response.setHighestAmountGoesTo(plusAmount);
            response.setHighestAmountDateTime(maxValueTime.toString());
            response.setLowestAmountDateTime(minValueTime.toString());
            response.setHighestPercentage(plusAmount.multiply(new BigDecimal(100)).divide(totalInvestment, RoundingMode.HALF_UP));
            response.setLowestPercentage(minusAmount.multiply(new BigDecimal(100)).divide(totalInvestment, RoundingMode.HALF_UP));
            //Graph
            optionsTickGraphLineList.add(ceGraphLine);
            optionsTickGraphLineList.add(peGraphLine);
            OptionsTickGraph optionsTickGraph = new OptionsTickGraph();
            optionsTickGraph.setOptionsTickGraphLineList(optionsTickGraphLineList);
            response.setOptionsTickGraph(optionsTickGraph);

            if(!isStopLossHit && !isProfitBooked) {
                BigDecimal differenceAmount = currentTotalValue.subtract(totalInvestment);
                response.setSellDateTime(peLastTradedTick.getDateTime().toString());
                response.setSellPercentage(differenceAmount.multiply(new BigDecimal(100)).divide(totalInvestment, RoundingMode.HALF_UP));
                response.setSellAmount(differenceAmount);

                response.setPeSellPrice(peCurrentPrice);
                response.setPeSellPricePerLot(pePerLotCurrentPrice);
                response.setPeSellAmount(peCurrentTotalValue);

                response.setCeSellPrice(ceCurrentPrice);
                response.setCeSellPricePerLot(cePerLotCurrentPrice);
                response.setCeSellAmount(ceCurrentTotalValue);
                
                response.setProfitOrLossAmount(differenceAmount);
                response.setProfitOrLossPercentage(differenceAmount.multiply(new BigDecimal(100)).divide(totalInvestment, RoundingMode.HALF_UP));
                response.setProfitOrLossDateTime(peLastTradedTick.getDateTime().toString());

                response.setDeInvestmentAmount(currentTotalValue);
                response.setBalanceAmount(initialAmount.subtract(differenceAmount.negate()));
            } 

            logs.close();
        } catch (IOException | StackOverflowError e) {
            if(logs != null) {
                try {
                    logs.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        
        return response;
    }

    private boolean checkStopLoss(BigDecimal stopLossPercentage, boolean isProfitBooked, boolean isStopLossHit,
            BigDecimal downPercentage) {
        return downPercentage.subtract(stopLossPercentage).doubleValue() >= 0 && !isStopLossHit && !isProfitBooked;
    }

    private boolean checkProfitBook(BigDecimal profitBookPercentage, boolean isProfitBooked, boolean isStopLossHit,
            BigDecimal upPercentage) {
        return upPercentage.subtract(profitBookPercentage).doubleValue() >= 0 && !isStopLossHit && !isProfitBooked;
    }

    private StrategyResponse getStrategyResponse(StrategyRequest request) {
        StrategyResponse strategyResponse = new StrategyResponse();
        strategyResponse.setId(new Date().toString());
        strategyResponse.setEntryDateTime(ModelUtil.convertDateTimeToShortString(request.getEntryDateTime()));
        strategyResponse.setExitDateTime(ModelUtil.convertDateTimeToShortString(request.getExitDateTime()));
        strategyResponse.setInitialAmount(request.getInitialAmount());
        strategyResponse.setStopLossPercentage(request.getStopLossPercentage());
        strategyResponse.setProfitBookPercentage(request.getProfitBookPercentage());
        strategyResponse.setQuantityOrPrice(request.getQuantityOrPrice());
        strategyResponse.setStrikeAwayPoints(request.getStrikeAwayPoints());
        return strategyResponse;
    }

    private OptionsTick getNotNullTickData(OptionsTick lastTradedTick, LocalDateTime dateTime, Map<LocalDateTime, OptionsTick> optionsTickMap, String optionType, FileWriter logs)
            throws IOException {
        if (optionsTickMap.get(dateTime) != null) {
            return optionsTickMap.get(dateTime);
        }
        
        if (dateTime.getHour() >= 15 && dateTime.getMinute() >= 30) {
            logs.append("Go beyond trading time " + optionType + " at " + dateTime.toString() + "\n");
            return lastTradedTick;
        }
        logs.append("Couldn't find price for the " + optionType + " at " + dateTime.toString() + "\n");
        return getNotNullTickData(lastTradedTick, dateTime.plusSeconds(1), optionsTickMap, optionType, logs);
    }
}
