package ru.livescripts.tests.javamoney.service;

import ru.livescripts.tests.javamoney.dto.Currency;

import java.util.Collection;

/**
 * @author Artem Areshko
 * 07.03.2018
 */
public interface CurrencyService {
    Collection<Currency> getAll();
    Currency getByCode(String code);
}
