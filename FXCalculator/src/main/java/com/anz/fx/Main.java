package com.anz.fx;

import com.anz.fx.exception.RateNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class Main {

    private static List<CurrencyPair> currencyPairList = new ArrayList<>();
    private static Map<String, Function<String, String>> crossViaMap = new HashMap<>();

    static {
        currencyPairList.add(new CurrencyPair("AUD", "USD", 0.8371));
        currencyPairList.add(new CurrencyPair("CAD", "USD", 0.8711));
        currencyPairList.add(new CurrencyPair("USD", "CNY", 6.1715));
        currencyPairList.add(new CurrencyPair("EUR", "USD", 1.2315));
        currencyPairList.add(new CurrencyPair("GBP", "USD", 1.5683));
        currencyPairList.add(new CurrencyPair("NZD", "USD", 0.7750));
        currencyPairList.add(new CurrencyPair("USD", "JPY", 119.95));
        currencyPairList.add(new CurrencyPair("EUR", "CZK", 27.6028));
        currencyPairList.add(new CurrencyPair("EUR", "DKK", 7.4405));
        currencyPairList.add(new CurrencyPair("EUR", "NOK", 8.6651));

        crossViaMap.put("AUD", term -> "USD");
        crossViaMap.put("CAD", term -> "USD");
        crossViaMap.put("CNY", term -> "USD");
        crossViaMap.put("CZK", term -> List.of("DKK", "NOK", "USD").contains(term) ? "EUR" : "USD");
        crossViaMap.put("DKK", term -> List.of("CZK", "NOK", "USD").contains(term) ? "EUR" : "USD");
        crossViaMap.put("EUR", term -> "USD");
        crossViaMap.put("GBP", term -> "USD");
        crossViaMap.put("JPY", term -> "USD");
        crossViaMap.put("NOK", term -> List.of("CZK", "DKK", "USD").contains(term) ? "EUR" : "USD");
        crossViaMap.put("NZD", term -> "USD");
        crossViaMap.put("USD", term -> List.of("CZK", "DKK", "NOK").contains(term) ? "EUR" : "USD");

    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(" ");
            if (tokens.length < 4) {
                System.out.println("usage: <ccy1> <amount1> in <ccy2>");
                System.exit(1);
            }

            try {
                FXCalculator fxCalculator = new FXCalculator();
                FXRateService rateService = new FXRateService();
                rateService.setCurrencyPairList(currencyPairList);
                CurrencyCrossViaTable crossTable = new CurrencyCrossViaTable();
                crossTable.setCurrencyCrossViaMap(crossViaMap);
                rateService.setCurrencyCrossViaTable(crossTable);
                fxCalculator.setRateService(rateService);
                System.out.println(fxCalculator.getCurrencyConversion(tokens[0], new BigDecimal(tokens[1]), tokens[3]));
            } catch (NumberFormatException e) {
                System.out.println("usage: <ccy1> <amount1> in <ccy2>");
            } catch (RateNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }


}
