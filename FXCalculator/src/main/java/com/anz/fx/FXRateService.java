package com.anz.fx;

import com.anz.fx.exception.RateNotFoundException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FXRateService {

    private CurrencyCrossViaTable currencyCrossViaTable;
    private List<CurrencyPair> currencyPairList = new ArrayList<>();

    public void setCurrencyPairList(List<CurrencyPair> currencyPairList) {
        this.currencyPairList = currencyPairList;
    }

    public void setCurrencyCrossViaTable(CurrencyCrossViaTable currencyCrossViaTable) {
        this.currencyCrossViaTable = currencyCrossViaTable;
    }

    private void checkParam(Supplier<Boolean> conditionMet, String errorMessage) {
        if (conditionMet.get()) {
            throw new InvalidParameterException(errorMessage);
        }
    }

    public double findRate(String base, String term) throws RateNotFoundException {
        checkParam(() -> base == null || base.isBlank(), "base must not be blank");
        checkParam(() -> term == null || term.isBlank(), "term must not be blank");
        if (currencyPairList == null || currencyPairList.isEmpty()) {
            throw new IllegalStateException("currency pairs not configured");
        }

        if (currencyCrossViaTable == null) {
            throw new IllegalStateException("currency cross-via table not configured");
        }
        //check if unity and given currency is in the currency pair list
        if (base.equalsIgnoreCase(term) && currencyPairList.stream().anyMatch(currencyPair -> currencyPair.getBase().equalsIgnoreCase(base) || currencyPair.getTerm().equalsIgnoreCase(base))) {
            return 1;
        }

        //find any possible direct feed or inverted
        Optional<CurrencyPair> directOrInvertedCurrencyPairResult = findDirectOrInvertedCurrencyPair(base, term);
        if (directOrInvertedCurrencyPairResult.isPresent()) {
            CurrencyPair directOrInvertedCurrencyPair = directOrInvertedCurrencyPairResult.get();
            return getDirectOrInvertedRate(directOrInvertedCurrencyPair,base);
        }

        //loop recursively until direct pair found
        return  findCrossViaRate(base, term, 1d);
    }

    private double findCrossViaRate(String base, String term, double rate) throws RateNotFoundException {
        String crossVia = currencyCrossViaTable.findCrossViaCurrency(base, term);

        return findRecursiveCross(base, crossVia, rate) * findRecursiveCross(crossVia, term, rate);
    }

    private double findRecursiveCross(String base, String term, double rate) throws RateNotFoundException {
        Optional<CurrencyPair> result = findDirectOrInvertedCurrencyPair(base, term);
        if(result.isPresent()){
           CurrencyPair pair = result.get();
           return  rate * (pair.getBase().equalsIgnoreCase(base)? pair.getRate() : 1/pair.getRate());
        }
        return findCrossViaRate(base, term, rate);
    }

    private double getDirectOrInvertedRate(CurrencyPair currencyPair, String base){
        return (currencyPair.getBase().equalsIgnoreCase(base))? currencyPair.getRate(): 1/currencyPair.getRate();
    }

    private Optional<CurrencyPair> findDirectOrInvertedCurrencyPair(String base, String term) {
        return currencyPairList.stream().filter(
                currencyPair -> (currencyPair.getBase().equalsIgnoreCase(base) && currencyPair.getTerm().equalsIgnoreCase(term)) ||
                        (currencyPair.getTerm().equalsIgnoreCase(base) && currencyPair.getBase().equalsIgnoreCase(term))).findFirst();
    }
}
