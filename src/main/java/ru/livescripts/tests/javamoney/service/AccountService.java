package ru.livescripts.tests.javamoney.service;

import ru.livescripts.tests.javamoney.entity.Account;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Artem Areshko
 * 07.03.2018
 */
public interface AccountService {
    Collection<Account> getAll();
    Account get(UUID id);
    Account save(Account account);
}
