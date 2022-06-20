package com.anz.fx;

import com.anz.fx.exception.RateNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyCrossViaTableTest {

    CurrencyCrossViaTable objectUnderTest;

    @BeforeEach
    void setUp() {
        objectUnderTest = new CurrencyCrossViaTable();
        Map<String, Function<String, String>> map = new HashMap<>();
        map.put("NOK", term -> List.of("CZK","DKK", "EUR", "USD").contains(term)? "EUR": "USD");
        map.put("USD", term -> List.of("CZK","DKK", "NOK").contains(term)? "EUR": "USD");
        map.put("EUR", term -> List.of("CZK","DKK", "NOK","USD").contains(term)? "EUR": "USD");
        map.put("JPY", term -> "USD");
        map.put("AUD", term -> "USD");
        objectUnderTest.setCurrencyCrossViaMap(map);
    }


    @Test
    void findCrossViaCurrency_baseNotFound_throwException(){
        RateNotFoundException exception = assertThrows(RateNotFoundException.class, () -> objectUnderTest.findCrossViaCurrency("DKK", "EUR"));
        assertEquals("Unable to find rate for DKK/EUR", exception.getMessage());
    }

    @Test
    void findCrossViaCurrency_termNotFound_throwException(){
        RateNotFoundException exception = assertThrows(RateNotFoundException.class, () -> objectUnderTest.findCrossViaCurrency("EUR", "DKK"));
        assertEquals("Unable to find rate for EUR/DKK", exception.getMessage());
    }
}