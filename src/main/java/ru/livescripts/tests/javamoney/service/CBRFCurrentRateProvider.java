package ru.livescripts.tests.javamoney.service;

import org.javamoney.moneta.ExchangeRateBuilder;
import org.javamoney.moneta.spi.AbstractRateProvider;
import org.javamoney.moneta.spi.DefaultNumberValue;
import org.javamoney.moneta.spi.LoaderService;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.UnknownCurrencyException;
import javax.money.convert.*;
import javax.money.spi.Bootstrap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CBRFCurrentRateProvider extends AbstractRateProvider implements LoaderService.LoaderListener {
    private static final String BASE_CURRENCY_CODE = "RUB";
    public static final CurrencyUnit BASE_CURRENCY = Monetary.getCurrency(BASE_CURRENCY_CODE);

    /**
     * The resource id used for the LoaderService.
     */
    private static final String RESOURCE_ID = CBRFCurrentRateProvider.class.getSimpleName();

    private static final ProviderContext CONTEXT =
            ProviderContextBuilder.of("CBRF", RateType.DEFERRED).set("providerDescription", "Central Bank of Russia")
                    .set("days", 1).build();

    //private final Map<LocalDate, Map<String, ExchangeRate>> rates = new ConcurrentHashMap<>();
    private final Map<String, ExchangeRate> rates = new ConcurrentHashMap<>();
    private final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

    public CBRFCurrentRateProvider() {
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
        ExchangeRateBuilder exchangeRateBuilder = getBuilder(conversionQuery);
        ExchangeRate sourceRate = rates.get(conversionQuery.getBaseCurrency()
                .getCurrencyCode());
        ExchangeRate target = rates.get(conversionQuery.getCurrency().getCurrencyCode());
        return createExchangeRate(conversionQuery, exchangeRateBuilder, sourceRate, target);
    }

    private ExchangeRateBuilder getBuilder(ConversionQuery query) {
        ExchangeRateBuilder builder = new ExchangeRateBuilder(
                ConversionContextBuilder.create(getContext(), RateType.DEFERRED)
                        .set(LocalDate.now()).build());
        builder.setBase(query.getBaseCurrency());
        builder.setTerm(query.getCurrency());
        return builder;
    }

    private ExchangeRate createExchangeRate(ConversionQuery query,
                                            ExchangeRateBuilder builder, ExchangeRate sourceRate,
                                            ExchangeRate target) {

        if (areBothBaseCurrencies(query)) {
            builder.setFactor(DefaultNumberValue.ONE);
            return builder.build();
        } else if (BASE_CURRENCY_CODE.equals(query.getCurrency().getCurrencyCode())) {
            if (Objects.isNull(sourceRate)) {
                return null;
            }
            return reverse(sourceRate);
        } else if (BASE_CURRENCY_CODE.equals(query.getBaseCurrency()
                .getCurrencyCode())) {
            return target;
        } else {
            // Get Conversion base as derived rate: base -> EUR -> term
            ExchangeRate rate1 = getExchangeRate(
                    query.toBuilder().setTermCurrency(Monetary.getCurrency(BASE_CURRENCY_CODE)).build());
            ExchangeRate rate2 = getExchangeRate(
                    query.toBuilder().setBaseCurrency(Monetary.getCurrency(BASE_CURRENCY_CODE))
                            .setTermCurrency(query.getCurrency()).build());
            if (Objects.nonNull(rate1) && Objects.nonNull(rate2)) {
                builder.setFactor(multiply(rate1.getFactor(), rate2.getFactor()));
                builder.setRateChain(rate1, rate2);
                return builder.build();
            }
            throw new CurrencyConversionException(query.getBaseCurrency(),
                    query.getCurrency(), sourceRate.getContext());
        }
    }

    private boolean areBothBaseCurrencies(ConversionQuery query) {
        return BASE_CURRENCY_CODE.equals(query.getBaseCurrency().getCurrencyCode()) &&
                BASE_CURRENCY_CODE.equals(query.getCurrency().getCurrencyCode());
    }

    private ExchangeRate reverse(ExchangeRate rate) {
        if (Objects.isNull(rate)) {
            throw new IllegalArgumentException("Rate null is not reversible.");
        }
        return new ExchangeRateBuilder(rate).setRate(rate).setBase(rate.getCurrency()).setTerm(rate.getBaseCurrency())
                .setFactor(divide(DefaultNumberValue.ONE, rate.getFactor(), MathContext.DECIMAL64)).build();
    }

    private class RateHandler extends DefaultHandler {
        protected final Logger log = Logger.getLogger(getClass().getName());

        //private static final String VAL_CURS = "ValCurs";
        private static final String VALUTE = "Valute";
        private static final String CHAR_CODE = "CharCode";
        private static final String NOMINAL = "Nominal";
        private static final String VALUE = "Value";

        private final Map<String, ExchangeRate> rates;
        private final ProviderContext context;

        private class Valute {
            String charCode;
            Integer nominal;
            Double value;
        }

        //private LocalDate rateDate;
        private String currentElement;
        private Valute valute;

        RateHandler(Map<String, ExchangeRate> rates, ProviderContext context) {
            this.rates = rates;
            this.context = context;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            /*if (VAL_CURS.equals(qName)) {
                rateDate = LocalDate.parse(attributes.getValue("Date"), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } else*/
            if (VALUTE.equals(qName)) {
                valute = new Valute();
            } else if (valute != null) {
                currentElement = qName;
            }
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (Objects.nonNull(currentElement)) {
                switch (currentElement) {
                    case CHAR_CODE: {
                        valute.charCode = String.valueOf(ch, start, length);
                        break;
                    }
                    case NOMINAL: {
                        valute.nominal = Integer.parseInt(String.valueOf(ch, start, length));
                        break;
                    }
                    case VALUE: {
                        valute.value = Double.parseDouble(String.valueOf(ch, start, length).replace(",", "."));
                        break;
                    }
                }
            }
            super.characters(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            currentElement = "";
            if (VALUTE.equals(qName)) {
                addRate();
                valute = null;
            }
            super.endElement(uri, localName, qName);
        }

        void addRate() {
            ConversionContext conversionContext = ConversionContextBuilder.create(context, RateType.DEFERRED)
                    .set(LocalDate.now())
                    .build();

            try {
                ExchangeRate exchangeRate = new ExchangeRateBuilder(conversionContext)
                        .setBase(BASE_CURRENCY)
                        .setTerm(Monetary.getCurrency(valute.charCode))
                        .setFactor(DefaultNumberValue.of(valute.nominal / valute.value))
                        .build();
                rates.put(valute.charCode, exchangeRate);
            } catch (UnknownCurrencyException e) {
                log.log(Level.INFO, e.getMessage());
            }
        }

    }
}
