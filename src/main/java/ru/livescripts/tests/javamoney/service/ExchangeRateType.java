package ru.livescripts.tests.javamoney.service;

import javax.money.convert.ExchangeRateProviderSupplier;

/**
 * @author Artem Areshko
 * 22.03.2018
 */
public enum ExchangeRateType implements ExchangeRateProviderSupplier {
    CBRF("CBRF");

    private final String type;

    ExchangeRateType(String type) {
        this.type = type;
    }

    @Override
    public String get() {
        return type;
    }
}
