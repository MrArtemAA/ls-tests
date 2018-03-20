package ru.livescripts.tests.javamoney.formatter;

import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

/**
 * @author Artem Areshko
 * 20.03.2018
 */
public class MonetaryAmountFormatFactory implements org.zalando.jackson.datatype.money.MonetaryAmountFormatFactory {
    @Override
    public MonetaryAmountFormat create(Locale locale) {
        return MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder.of(locale)
                        .set(CurrencyStyle.NAME)
                        .set("pattern", "0.00 ¤")
                        .build());
    }
}
