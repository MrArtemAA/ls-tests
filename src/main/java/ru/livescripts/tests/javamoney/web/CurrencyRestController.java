package ru.livescripts.tests.javamoney.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.livescripts.tests.javamoney.dto.Currency;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = "api/v1/currencies", method = GET)
public class CurrencyRestController {

    public Iterable<Currency> getAll() {
        Collection<CurrencyUnit> currenciesByDefaultProvider = Monetary.getCurrencies("default");
        Locale locale = Locale.getDefault();
        return currenciesByDefaultProvider.stream()
                .map(currencyUnit -> {
                    String currencyCode = currencyUnit.getCurrencyCode();
                    java.util.Currency javaCurrency = java.util.Currency.getInstance(currencyCode);
                    String currencyDisplayName = javaCurrency.getDisplayName(locale);
                    Currency currency = new Currency();
                    currency.name = currencyDisplayName;
                    currency.code = currencyCode;
                    return currency;
                })
                .collect(Collectors.toList());
    }

}
