package com.anz.fx;

import com.anz.fx.exception.RateNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FXRateServiceTest {

    FXRateService objectUnderTest;
    CurrencyCrossViaTable currencyCrossViaTable;
    public static String[] nullOrEmptyStrings(){
        return new String[]{ null , "", " "};
    }


    @BeforeEach
    void setUp() {
        objectUnderTest = new FXRateService();
        List<CurrencyPair> currencyPairList = new ArrayList<>();
        currencyPairList.add(new CurrencyPair("AUD", "USD", 0.8371));
        currencyPairList.add(new CurrencyPair("CAD", "USD", 0.8711));
        currencyPairList.add(new CurrencyPair("EUR", "DKK", 7.4405));
        currencyPairList.add(new CurrencyPair("EUR", "USD", 1.2315));

        objectUnderTest.setCurrencyPairList(currencyPairList);
        currencyCrossViaTable =  mock(CurrencyCrossViaTable.class);
        objectUnderTest.setCurrencyCrossViaTable(currencyCrossViaTable);
    }

    @Test
    void findRate_crossVia() throws RateNotFoundException {
        when(currencyCrossViaTable.findCrossViaCurrency("AUD", "DKK"))
                .thenReturn("USD");
        when(currencyCrossViaTable.findCrossViaCurrency("USD", "DKK"))
                .thenReturn("EUR");

        double rate = objectUnderTest.findRate("AUD", "DKK");
        assertEquals(5.057606617945594, rate);
    }

    @ParameterizedTest
    @MethodSource( value = "nullOrEmptyStrings")
    void findRate_baseIsNullOrBlank_shouldThrowException(String base){
        InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> {
            objectUnderTest.findRate(base, "USD");
        });

        assertEquals("base must not be blank", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource( value = "nullOrEmptyStrings")
    void findRate_termIsNullOrBlank_shouldThrowException(String term){
        InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> {
            objectUnderTest.findRate("AUD", term);
        });

        assertEquals("term must not be blank", exception.getMessage());
    }


    @Test
    void findRate_unity_shouldReturn1() throws RateNotFoundException {
        String currency = "AUD";
        double rate = objectUnderTest.findRate(currency, currency);
        assertEquals(1, rate);
    }

    @Test
    void findRate_directFeed() throws RateNotFoundException {
        double rate = objectUnderTest.findRate("AUD", "USD");
        assertEquals(0.8371, rate);
    }


    @Test
    void findRate_inverted() throws RateNotFoundException {
        double rate = objectUnderTest.findRate("USD", "AUD");
        assertEquals(1.1946004061641382, rate);
    }


}