package domain;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Currency {
    DOLLAR("$"),
    EURO("€"),
    RUB("₽");

    private final String display;
    private static final Map<String, Currency> display2val = init();

    private static Map<String, Currency> init() {
        Map<String, Currency> dict = Arrays.stream(Currency.values()).collect(Collectors.toMap(Currency::show, x -> x));
        dict.put("dollar", DOLLAR);
        dict.put("rub", RUB);
        dict.put("euro", EURO);
        return dict;
    }

    Currency(String display) {
        this.display = display;
    }

    public String show() {
        return display;
    }

    public static Currency fromString(String s) {
        return display2val.get(s);
    }
}
