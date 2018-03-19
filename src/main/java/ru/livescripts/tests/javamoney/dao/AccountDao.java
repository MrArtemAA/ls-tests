package ru.livescripts.tests.javamoney.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.livescripts.tests.javamoney.entity.Account;

import java.util.UUID;

/**
 * @author Artem Areshko
 * 07.03.2018
 */
@Repository
public interface AccountDao extends CrudRepository<Account, UUID> {
}
