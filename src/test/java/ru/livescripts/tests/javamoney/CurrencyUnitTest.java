package ru.livescripts.tests.javamoney;

import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

//http://www.baeldung.com/java-money-and-currency
//https://www.mscharhag.com/java/java-jsr-354-money-and-currency-api

public class CurrencyUnitTest {
    private static final String PROVIDER_NAME_DEFAULT = "default";

    @Test
    public void testGetCurrencyProviderNames() {
        Set<String> currencyProviderNames = Monetary.getCurrencyProviderNames();
        assertNotNull(currencyProviderNames);
        assertFalse(currencyProviderNames.isEmpty());

        printCurrencyProviderNames(currencyProviderNames);
    }

    private void printCurrencyProviderNames(Set<String> currencyProviderNames) {
        System.out.println("Available currency providers: ");
        currencyProviderNames.forEach(System.out::println);
        printEndSeparator();
    }

    @Test
    public void testGetAllAvailableCurrencies() {
        Collection<CurrencyUnit> currenciesByDefaultProvider = Monetary.getCurrencies(PROVIDER_NAME_DEFAULT);
        assertNotNull(currenciesByDefaultProvider);
        assertFalse(currenciesByDefaultProvider.isEmpty());

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
        assertTrue(Monetary.isCurrencyAvailable(ruLocale, PROVIDER_NAME_DEFAULT));
        assertTrue(Monetary.isCurrencyAvailable("RUB", PROVIDER_NAME_DEFAULT));
    }

}
