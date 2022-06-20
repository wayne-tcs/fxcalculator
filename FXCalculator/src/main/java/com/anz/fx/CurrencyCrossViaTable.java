package com.anz.fx;

import com.anz.fx.exception.RateNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CurrencyCrossViaTable {

    private Map<String, Function<String, String>> currencyCrossViaMap = new HashMap<>();

    public String findCrossViaCurrency(String base, String term) throws RateNotFoundException {
        if(!currencyCrossViaMap.containsKey(base) || !currencyCrossViaMap.containsKey(term)){
            throw new RateNotFoundException("Unable to find rate for "+base + "/" + term);
        }

        return currencyCrossViaMap.get(base).apply(term);

//        return new CrossViaResult(currencyCrossViaMap.get(base).apply(crossViaCurrency),
 //               currencyCrossViaMap.get(crossViaCurrency).apply(term));
    }

    public void setCurrencyCrossViaMap(Map<String, Function<String, String>> currencyCrossViaMap) {
        this.currencyCrossViaMap = currencyCrossViaMap;
    }

    static final class CrossViaResult{
        private final String baseCrossVia;
        private final String termCrossVia;

        public CrossViaResult(String baseCrossVia, String termCrossVia) {
            this.baseCrossVia = baseCrossVia;
            this.termCrossVia = termCrossVia;
        }

        public String getBaseCrossVia() {
            return baseCrossVia;
        }

        public String getTermCrossVia() {
            return termCrossVia;
        }
    }
}
