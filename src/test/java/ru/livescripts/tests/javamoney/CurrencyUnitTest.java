package ru.livescripts.tests.javamoney;

import org.junit.Assert;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.*;

//http://www.baeldung.com/java-money-and-currency
//https://www.mscharhag.com/java/java-jsr-354-money-and-currency-api

public class CurrencyUnitTest {

    @Test
    public void testGetAllAvailableCurrencies() {
        Set<String> currencyProviderNames = Monetary.getCurrencyProviderNames();
        System.out.println(currencyProviderNames);
        Collection<CurrencyUnit> currencies = Monetary.getCurrencies(currencyProviderNames.toArray(new String[currencyProviderNames.size()]));
        System.out.println(currencies.size());
        System.out.println(currencies);

        Collection<CurrencyUnit> currenciesByDefaultProvider = Monetary.getCurrencies("default");
        System.out.println(currenciesByDefaultProvider.size());

        Locale ruLocale = new Locale.Builder().setLanguage("ru").setRegion("RU").build();
        CurrencyUnit ruCurrencyUnit = Monetary.getCurrency(ruLocale);
        System.out.format("%s, %s", ruCurrencyUnit.toString(), Currency.getInstance(ruCurrencyUnit.getCurrencyCode()).getDisplayName(ruLocale));

        Assert.assertTrue(currencies.contains(ruCurrencyUnit));
    }

}
