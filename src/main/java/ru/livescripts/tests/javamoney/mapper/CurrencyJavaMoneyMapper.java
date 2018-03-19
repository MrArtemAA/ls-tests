package ru.livescripts.tests.javamoney.mapper;

import org.springframework.stereotype.Component;
import ru.livescripts.tests.javamoney.dto.Currency;

import javax.money.CurrencyUnit;

import static ru.livescripts.tests.javamoney.util.CurrencyUtil.getCurrencyDisplayName;

/**
 * @author Artem Areshko
 * 07.03.2018
 */
@Component
public class CurrencyJavaMoneyMapper {
    public Currency toDto(CurrencyUnit currencyUnit) {
        Currency currency = new Currency();
        currency.name = getCurrencyDisplayName(currencyUnit);
        currency.code = currencyUnit.getCurrencyCode();
        return currency;
    }
}
