package com.anz.fx;


import com.anz.fx.exception.RateNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FXCalculator {

    enum CurrencyPrecision{
        AUD(2),
        CAD(2),
        CNY(2),
        CZK(2),
        DKK(2),
        EUR(2),
        GBP(2),
        JPY(0),
        NOK(2),
        NZD(2),
        USD(2);

        int precision;

        CurrencyPrecision(int precision) {
            this.precision = precision;
        }
    }

    public FXCalculator() {
    }

    public FXRateService getRateService() {
        return rateService;
    }

    public void setRateService(FXRateService rateService) {
        this.rateService = rateService;
    }

    private FXRateService rateService;

    public String getCurrencyConversion(String currencyFrom, BigDecimal amount, String currencyTo) throws RateNotFoundException {
        //get rate
        double rates = rateService.findRate(currencyFrom, currencyTo);

        int currencyFromPrecision = CurrencyPrecision.valueOf(currencyFrom).precision;
        int currencyToPrecision = CurrencyPrecision.valueOf(currencyTo).precision;

        StringBuilder sb = new StringBuilder();
        sb.append(currencyFrom.toUpperCase()).append(" ")
                .append(amount.setScale(currencyFromPrecision, RoundingMode.HALF_UP))
                .append(" = ").append(currencyTo).append(" ")
                .append(amount.multiply(BigDecimal.valueOf(rates))
                        .setScale(currencyToPrecision, RoundingMode.HALF_UP));
        return  sb.toString();
    }

}
