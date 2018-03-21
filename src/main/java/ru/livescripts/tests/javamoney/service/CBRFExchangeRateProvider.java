package ru.livescripts.tests.javamoney.service;

import org.javamoney.moneta.internal.convert.ECBCurrentRateProvider;
import org.javamoney.moneta.spi.AbstractRateProvider;
import org.javamoney.moneta.spi.LoaderService;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.convert.*;
import javax.money.spi.Bootstrap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CBRFExchangeRateProvider extends AbstractRateProvider implements LoaderService.LoaderListener {
    private static final String BASE_CURRENCY_CODE = "RUB";
    public static final CurrencyUnit BASE_CURRENCY = Monetary.getCurrency(BASE_CURRENCY_CODE);

    /**
     * The resource id used for the LoaderService.
     */
    private static final String RESOURCE_ID = ECBCurrentRateProvider.class.getSimpleName();

    private static final ProviderContext CONTEXT =
            ProviderContextBuilder.of("CBRF", RateType.DEFERRED).set("providerDescription", "Central Bank of Russia")
                    .set("days", 1).build();

    private final Map<LocalDate, Map<String, ExchangeRate>> rates = new ConcurrentHashMap<>();
    private final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

    public CBRFExchangeRateProvider() {
        super(CONTEXT);
        saxParserFactory.setNamespaceAware(false);
        saxParserFactory.setValidating(false);
        LoaderService loader = Bootstrap.getService(LoaderService.class);
        loader.addLoaderListener(this, RESOURCE_ID);
        loader.loadDataAsync(RESOURCE_ID);
    }


    @Override
    public void newDataLoaded(String resourceId, InputStream is) {
        try {
            SAXParser parser = saxParserFactory.newSAXParser();
            parser.parse(is, new RateHandler(rates, CONTEXT));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ExchangeRate getExchangeRate(ConversionQuery conversionQuery) {
        Objects.requireNonNull(conversionQuery);
        if (rates.isEmpty()) {
            return null;
        }
        return null;
    }

    private class RateHandler extends DefaultHandler {
        private final Map<LocalDate, Map<String, ExchangeRate>> historicRates;
        private final ProviderContext context;

        RateHandler(Map<LocalDate, Map<String, ExchangeRate>> historicRates, ProviderContext context) {
            this.historicRates = historicRates;
            this.context = context;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            super.startElement(uri, localName, qName, attributes);
        }
    }
}
