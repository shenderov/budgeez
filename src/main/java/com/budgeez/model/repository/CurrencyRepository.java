package com.budgeez.model.repository;

import com.budgeez.model.entities.dao.Currency;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    Currency save(Currency currency);

    Currency findByCurrencyCode(String currencyCode);

    @Modifying
    @Transactional
    void deleteByCurrencyCode(String currencyCode);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE currency SET currency_code=?2 WHERE currency_code=?1")
    void setCurrencyCode(String currencyCode, String newCurrencyCode);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE currency SET currency_name=?2 WHERE currency_code=?1")
    void setCurrencyName(String currencyCode, String newCurrencyName);
}
