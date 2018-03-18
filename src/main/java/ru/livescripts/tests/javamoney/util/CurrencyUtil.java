package ru.livescripts.tests.javamoney.util;

import javax.money.CurrencyUnit;
import java.util.Currency;
import java.util.Locale;

public final class CurrencyUtil {
    private static final Locale LOCALE_DEFAULT = Locale.getDefault();

    private CurrencyUtil() {}

    public static String getCurrencyDisplayName(CurrencyUnit currencyUnit) {
        Currency javaCurrency = Currency.getInstance(currencyUnit.getCurrencyCode());
        return javaCurrency.getDisplayName(LOCALE_DEFAULT);
    }
}
