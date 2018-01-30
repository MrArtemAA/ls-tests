package ru.livescripts.tests.javamoney;

import org.javamoney.moneta.format.CurrencyStyle;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

//https://github.com/JavaMoney/jsr354-ri/issues/161
//https://stackoverflow.com/questions/46150919/java-money-currency-conversion-rate-on-specific-date

public class CurrencyConversionTest {

    @Test
    public void testGetEURAndUSDExchangeRate() {
        CurrencyUnit eur = Monetary.getCurrency("EUR");
        CurrencyUnit usd = Monetary.getCurrency("USD");

        ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider();
        ExchangeRate eurUsdRate = exchangeRateProvider.getExchangeRate(eur, usd);
        System.out.println(eurUsdRate.getFactor());

        ExchangeRate usdEurRate = exchangeRateProvider.getExchangeRate(usd, eur);
        System.out.println(usdEurRate.getFactor());
    }

    @Test
    public void testGetExchangeRate() {
        CurrencyUnit rub = Monetary.getCurrency("RUB");
        CurrencyUnit usd = Monetary.getCurrency("USD");

        ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider();
        ExchangeRate rubUsdRate = exchangeRateProvider.getExchangeRate(rub, usd);
        System.out.println(rubUsdRate.getFactor());

        ExchangeRate usdRubRate = exchangeRateProvider.getExchangeRate(usd, rub);
        System.out.println(usdRubRate.getFactor());
    }

    @Test
    public void testCurrencyConversion() {
        CurrencyUnit rub = Monetary.getCurrency("RUB");
        CurrencyUnit usd = Monetary.getCurrency("USD");

        MonetaryAmount usdAmount = Monetary.getDefaultAmountFactory()
                .setCurrency(usd)
                .setNumber(10)
                .create();

        CurrencyConversion currencyConversion = MonetaryConversions.getConversion(rub);

        MonetaryAmount rubAmount = usdAmount.with(currencyConversion);
        System.out.println(rubAmount);

        MonetaryAmountFormat customFormat = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder.of(Locale.getDefault())
                        .set(CurrencyStyle.NAME)
                        .set("pattern", "0.00 ¤")
                        .build());

        System.out.println(customFormat.format(rubAmount));
    }

}
