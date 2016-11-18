package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, String> {
    Currency save(Currency currency);
}
