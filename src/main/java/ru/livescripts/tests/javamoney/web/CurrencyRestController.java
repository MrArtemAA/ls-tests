package ru.livescripts.tests.javamoney.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.livescripts.tests.javamoney.dto.Currency;
import ru.livescripts.tests.javamoney.service.CurrencyService;

@RestController
@RequestMapping(CurrencyRestController.URL)
public class CurrencyRestController {
    static final String URL = "/api/v1/javamoney/currencies";

    private final CurrencyService currencyService;

    public CurrencyRestController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public Iterable<Currency> getAll() {
        return currencyService.getAll();
    }

    @GetMapping("/{code}")
    public Currency getByCode(@PathVariable("code") String code) {
        return currencyService.getByCode(code);
    }

}
