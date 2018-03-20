package ru.livescripts.tests.javamoney.testsuite;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

/**
 * @author Artem Areshko
 * 20.03.2018
 */
public final class MonetaryAmountUtil {
    private MonetaryAmountUtil() {}

    public static MonetaryAmount createMonetaryAmount(CurrencyUnit currencyUnit, Number number) {
        return Monetary.getDefaultAmountFactory()
                .setCurrency(currencyUnit)
                .setNumber(number)
                .create();
    }
}
