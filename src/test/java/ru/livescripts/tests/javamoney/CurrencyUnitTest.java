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
    public void testGetCurrencyProviderNames() {
        Set<String> currencyProviderNames = Monetary.getCurrencyProviderNames();
        Assert.assertNotNull(currencyProviderNames);
        Assert.assertFalse(currencyProviderNames.isEmpty());

        printCurrencyProviderNames(currencyProviderNames);
    }

    private void printCurrencyProviderNames(Set<String> currencyProviderNames) {
        System.out.println("Available currency providers: ");
        currencyProviderNames.forEach(System.out::println);
        printEndSeparator();
    }

    @Test
    public void testGetAllAvailableCurrencies() {
        Collection<CurrencyUnit> currenciesByDefaultProvider = Monetary.getCurrencies("default");
        Assert.assertNotNull(currenciesByDefaultProvider);
        Assert.assertFalse(currenciesByDefaultProvider.isEmpty());

        printAvailableCurrencies(currenciesByDefaultProvider);
    }

    private void printAvailableCurrencies(Collection<CurrencyUnit> currencies) {
        System.out.format("Available currencies (total: %d): \n", currencies.size());
        int i = 1;
        for (CurrencyUnit currencyUnit : currencies) {
            System.out.print(currencyUnit.getCurrencyCode() + ", ");
            if (i % 10 == 0) {
                System.out.println();
            }
            i++;
        }
        System.out.println();
        printEndSeparator();
    }

    private void printEndSeparator() {
        System.out.println(String.join("", Collections.nCopies(30, "=")));
    }

    @Test
    public void testRuCurrencyIsProvided() {
        Locale ruLocale = new Locale("ru","RU");
        Assert.assertTrue(Monetary.isCurrencyAvailable(ruLocale, "default"));
        Assert.assertTrue(Monetary.isCurrencyAvailable("RUB", "default"));
    }

}
