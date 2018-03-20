package ru.livescripts.tests.javamoney;

import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.livescripts.tests.javamoney.testsuite.MonetaryAmountUtil.createMonetaryAmount;

//https://github.com/JavaMoney/jsr354-ri/issues/161
//https://stackoverflow.com/questions/46150919/java-money-currency-conversion-rate-on-specific-date

public class CurrencyConversionTest {
    private static final CurrencyUnit EUR = Monetary.getCurrency("EUR");
    private static final CurrencyUnit USD = Monetary.getCurrency("USD");

    @Test
    public void testGetExchangeRateProvider() {
        ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider();
        assertNotNull(exchangeRateProvider);
    }

    @Test
    public void testGetExchangeRate() {
        ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider();
        ExchangeRate eurToUsdRate = exchangeRateProvider.getExchangeRate(EUR, USD);
        ExchangeRate usdToEurRate = exchangeRateProvider.getExchangeRate(USD, EUR);

        assertNotNull(eurToUsdRate);
        assertNotNull(usdToEurRate);
    }

    @Test
    public void testCurrencyConversion() {
        MonetaryAmount usdAmount = createMonetaryAmount(USD, 10);

        CurrencyConversion toEurConversion = MonetaryConversions.getConversion(EUR);
        assertNotNull(toEurConversion);

        MonetaryAmount eurAmount = usdAmount.with(toEurConversion);
        assertNotNull(eurAmount);
        assertEquals(EUR, eurAmount.getCurrency());

        double backToUsdNumber = eurAmount.getNumber().doubleValue() /
                toEurConversion.getExchangeRate(usdAmount).getFactor().doubleValue();
        assertEquals(usdAmount.getNumber().doubleValue(), backToUsdNumber, 0);
    }

}
