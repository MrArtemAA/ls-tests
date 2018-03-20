package ru.livescripts.tests.javamoney;

import org.javamoney.moneta.format.CurrencyStyle;
import org.junit.Assert;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static ru.livescripts.tests.javamoney.testsuite.MonetaryAmountUtil.createMonetaryAmount;
import static ru.livescripts.tests.javamoney.util.CurrencyUtil.getCurrencyDisplayName;

public class MonetaryAmountTest {
    private static final CurrencyUnit CURRENCY_UNIT = Monetary.getCurrency(Locale.getDefault());

    @Test
    public void testCreate() {
        MonetaryAmount monetaryAmount = createMonetaryAmount(CURRENCY_UNIT, 10.10);

        assertEquals(CURRENCY_UNIT.getCurrencyCode(), monetaryAmount.getCurrency().getCurrencyCode());
        assertEquals(10.10, monetaryAmount.getNumber().doubleValue(), 0);
    }

    @Test
    public void testPrecision() {
        MonetaryAmount monetaryAmount = createMonetaryAmount(CURRENCY_UNIT, 0.10);
        MonetaryAmount resultAmount = createMonetaryAmount(CURRENCY_UNIT, 0);

        for (int i = 0; i < 10; i++) {
            resultAmount = resultAmount.add(monetaryAmount);
        }
        assertEquals(1, resultAmount.getNumber().doubleValue(), 0);
    }

    @Test
    public void testDoublePrecisionFailure() {
        double val = 0.00;
        for (int i = 0; i < 10; i++) {
            val += 0.10;
        }
        Assert.assertFalse(1.00 == val);
    }

    @Test
    public void testFormatting() {
        MonetaryAmount monetaryAmount = createMonetaryAmount(CURRENCY_UNIT, 10.20);
        MonetaryAmountFormat customFormat = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder.of(Locale.getDefault())
                        .set(CurrencyStyle.NAME)
                        .set("pattern", "0.00 ¤")
                        .build());

        System.out.println(customFormat.format(monetaryAmount));
        assertEquals(String.format("%.2f %s", 10.20, getCurrencyDisplayName(CURRENCY_UNIT)),
                customFormat.format(monetaryAmount));
    }

}
