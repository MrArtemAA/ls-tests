package ru.livescripts.tests.javamoney;

import org.javamoney.moneta.spi.AbstractRateProvider;

import javax.money.convert.ProviderContext;

public abstract class CBRFExchangeRateProvider extends AbstractRateProvider {
    /**
     * Constructor.
     *
     * @param providerContext the {@link ProviderContext}, not null.
     */
    public CBRFExchangeRateProvider(ProviderContext providerContext) {
        super(providerContext);
    }
}
