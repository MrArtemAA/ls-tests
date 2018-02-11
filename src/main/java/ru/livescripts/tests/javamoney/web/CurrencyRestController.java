package ru.livescripts.tests.javamoney.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.livescripts.tests.javamoney.dto.Currency;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyRestController {

    @GetMapping
    public Iterable<Currency> getAll() {
        Collection<CurrencyUnit> currenciesByDefaultProvider = Monetary.getCurrencies("default");
        Locale locale = Locale.getDefault();
        return currenciesByDefaultProvider.stream()
                .map(this::getCurrency)
                .collect(Collectors.toList());
    }

    @GetMapping("/{code}")
    public Currency getByCode(@PathVariable("code") String code) {
        CurrencyUnit currencyUnit = Monetary.getCurrency(code.toUpperCase(), "default");
        return getCurrency(currencyUnit);
    }

    private Currency getCurrency(CurrencyUnit currencyUnit) {
        String currencyCode = currencyUnit.getCurrencyCode();
        Currency currency = new Currency();
        currency.name = getCurrencyDisplayName(currencyCode);
        currency.code = currencyCode;
        return currency;
    }

    private String getCurrencyDisplayName(String currencyCode) {
        java.util.Currency javaCurrency = java.util.Currency.getInstance(currencyCode);
        return javaCurrency.getDisplayName(Locale.getDefault());
    }

}
