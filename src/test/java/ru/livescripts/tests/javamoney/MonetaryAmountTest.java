package ru.livescripts.tests.javamoney;

import org.junit.Assert;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Locale;

public class MonetaryAmountTest {

    @Test
    public void testMonetaryAmount() {
        CurrencyUnit currencyUnit = Monetary.getCurrency(Locale.getDefault());
        MonetaryAmount monetaryAmount = Monetary.getDefaultAmountFactory()
                .setCurrency(currencyUnit)
                .setNumber(0.10)
                .create();

        System.out.println(monetaryAmount.getClass());

        MonetaryAmount resultAmount = Monetary.getDefaultAmountFactory()
                .setCurrency(currencyUnit)
                .setNumber(0.0)
                .create();

        for (int i = 0; i < 10; i++) {
            resultAmount = resultAmount.add(monetaryAmount);
        }
        System.out.println(resultAmount.getNumber());
        Assert.assertTrue(1.00 == resultAmount.getNumber().doubleValue());
    }

    @Test
    public void testDoubleFailure() {
        double val = 0.00;
        for (int i = 0; i < 10; i++) {
            val += 0.10;
        }
        Assert.assertFalse(1.00 == val);
    }

}
