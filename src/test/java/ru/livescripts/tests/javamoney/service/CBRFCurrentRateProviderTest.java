package ru.livescripts.tests.javamoney.service;

import org.javamoney.moneta.spi.MonetaryConfig;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import static org.junit.Assert.assertEquals;

/**
 * @author Artem Areshko
 * 22.03.2018
 */
public class CBRFCurrentRateProviderTest {
    private static final CurrencyUnit RUB = Monetary.getCurrency("RUB");
    private static final CurrencyUnit USD = Monetary.getCurrency("USD");
    private static final CurrencyUnit EUR = Monetary.getCurrency("EUR");

    @Test
    public void testGetExchangeRateProviderChain() {
        System.out.println(MonetaryConfig.getConfig().get("conversion.default-chain"));
        System.out.println(MonetaryConfig.getConfig().get("load.ECBCurrentRateProvider.urls"));
    }

    @Test
    public void testGetExchangeRate() throws Exception {
        CBRFCurrentRateProvider cbrfCurrentRateProvider = new CBRFCurrentRateProvider();
        //give some time to load data async
        Thread.sleep(2000);
        ExchangeRate rubToEurRate = cbrfCurrentRateProvider.getExchangeRate(RUB, EUR);
        System.out.println(rubToEurRate.getFactor());
        ExchangeRate usdToRubRate = cbrfCurrentRateProvider.getExchangeRate(USD, RUB);
        System.out.println(usdToRubRate.getFactor());
    }

    @Test
    public void testGetCBRFProviderByName() {
        ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider(ExchangeRateType.CBRF);
        assertEquals(CBRFCurrentRateProvider.class, exchangeRateProvider.getClass());
    }

}