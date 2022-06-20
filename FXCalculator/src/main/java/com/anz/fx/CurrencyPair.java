package com.anz.fx;

import java.util.Objects;

public final class CurrencyPair {
    private final String base;
    private final String term;
    private final double rate;

    public CurrencyPair(String base, String term, double rate) {
        this.base = base;
        this.term = term;
        this.rate = rate;
    }

    public String getBase() {
        return base;
    }

   public String getTerm() {
        return term;
    }

   public double getRate() {
        return rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyPair that = (CurrencyPair) o;
        return Double.compare(that.getRate(), getRate()) == 0 && getBase().equals(that.getBase()) && getTerm().equals(that.getTerm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBase(), getTerm(), getRate());
    }
}
