package service;

import domain.Currency;

import java.util.Map;

public class CurrencyConverter {
    private static final Map<Currency, Double> cost2dollar = Map.ofEntries(Map.entry(Currency.DOLLAR, 1.0), Map.entry(Currency.EURO, 1.2), Map.entry(Currency.RUB, 0.014));

    public static Double convert(Double amount, Currency initial, Currency convert) {
        return amount * cost2dollar.get(initial) / cost2dollar.get(convert);
    }
}
