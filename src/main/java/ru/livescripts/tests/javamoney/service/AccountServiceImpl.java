package ru.livescripts.tests.javamoney.service;

import org.springframework.stereotype.Service;
import ru.livescripts.tests.javamoney.dao.AccountDao;
import ru.livescripts.tests.javamoney.entity.Account;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Artem Areshko
 * 07.03.2018
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountDao dao;

    public AccountServiceImpl(AccountDao dao) {
        this.dao = dao;
    }

    @Override
    public Collection<Account> getAll() {
        return (Collection<Account>) dao.findAll();
    }

    @Override
    public Account get(UUID id) {
        Account account = dao.findOne(id);
        if (account == null) {
            throw new RuntimeException("Account not found with id = " + id);
        }
        return account;
    }

    @Override
    public Account save(Account account) {
        return dao.save(account);
    }
}
