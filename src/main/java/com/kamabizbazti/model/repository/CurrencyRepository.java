package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, String> {
//public interface CurrencyRepository{
    Currency save(Currency currency);
    //void addCurrency (Currency currency);
   // Currency findCurrencyByCurrencyCode(String currencyCode);

}
