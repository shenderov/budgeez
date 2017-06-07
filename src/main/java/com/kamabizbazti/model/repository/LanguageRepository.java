package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.dao.Language;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long> {
    Language save(Language language);

    Language findByLanguageCode(String languageCode);

    @Modifying
    @Transactional
    void deleteByLanguageCode(String languageCode);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE language SET language_code=?2 WHERE language_code=?1")
    void setLanguageCode(String currencyCode, String newCurrencyCode);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE language SET language_name=?2 WHERE language_code=?1")
    void setLanguageName(String languageCode, String newLanguageyName);
}