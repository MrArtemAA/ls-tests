package ru.livescripts.tests.javamoney.service;

import org.springframework.stereotype.Service;
import ru.livescripts.tests.javamoney.dto.Currency;
import ru.livescripts.tests.javamoney.mapper.CurrencyJavaMoneyMapper;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Artem Areshko
 * 07.03.2018
 */
@Service
public class CurrencyJavaMoneyService implements CurrencyService {
    private static final String CURRENCY_PROVIDER_DEFAULT = "default";

    private final CurrencyJavaMoneyMapper mapper;

    public CurrencyJavaMoneyService(CurrencyJavaMoneyMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Collection<Currency> getAll() {
        Collection<CurrencyUnit> currenciesByDefaultProvider = Monetary.getCurrencies(CURRENCY_PROVIDER_DEFAULT);
        return currenciesByDefaultProvider.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Currency getByCode(String code) {
        CurrencyUnit currencyUnit = Monetary.getCurrency(code.toUpperCase(), CURRENCY_PROVIDER_DEFAULT);
        return mapper.toDto(currencyUnit);
    }
}
