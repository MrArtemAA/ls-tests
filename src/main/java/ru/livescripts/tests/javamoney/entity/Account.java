package ru.livescripts.tests.javamoney.entity;

import org.javamoney.midas.javaee7.jpa.MoneyConverter;

import javax.money.MonetaryAmount;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author Artem Areshko
 * 07.03.2018
 */
@Entity
public class Account {
    @Id
    @GeneratedValue
    public UUID id;

    public String title;
    @Convert(converter = MoneyConverter.class)
    public MonetaryAmount amount;
}
