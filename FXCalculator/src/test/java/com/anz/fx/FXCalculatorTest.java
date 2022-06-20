package com.anz.fx;

import com.anz.fx.exception.RateNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FXCalculatorTest {

    FXCalculator calculator;

    FXRateService rateService;

    private String currencyFrom;
    private String currencyTo;

    @BeforeEach
    void setUp() {
        calculator = new FXCalculator();
        rateService = mock(FXRateService.class);
        calculator.setRateService(rateService);
        currencyFrom = "AUD";
        currencyTo = "USD";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCurrencyConversion_withRoundUp() throws RateNotFoundException {
        when(rateService.findRate(currencyFrom, currencyTo)).thenReturn(0.83715);
        String result =  calculator.getCurrencyConversion(currencyFrom, BigDecimal.valueOf(100), currencyTo);
        assertEquals("AUD 100.00 = USD 83.72", result);

        verify(rateService).findRate(currencyFrom, currencyTo);
    }
}