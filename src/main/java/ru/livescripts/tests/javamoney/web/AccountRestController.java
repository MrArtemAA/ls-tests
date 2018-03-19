package ru.livescripts.tests.javamoney.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.livescripts.tests.javamoney.entity.Account;
import ru.livescripts.tests.javamoney.service.AccountService;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Artem Areshko
 * 07.03.2018
 */
@RestController
@RequestMapping(AccountRestController.URL)
public class AccountRestController {
    static final String URL = "/api/v1/javamoney/accounts";

    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Collection<Account> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public Account get(@PathVariable("id") UUID id) {
        return accountService.get(id);
    }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account, UriComponentsBuilder uriComponentsBuilder) {
        Account created = accountService.save(account);

        URI uriOfNewResource = uriComponentsBuilder
                .path(URL + "/{id}")
                .buildAndExpand(created.id)
                .toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
