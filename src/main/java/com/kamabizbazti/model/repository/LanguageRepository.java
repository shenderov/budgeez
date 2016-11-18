package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.Language;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends CrudRepository<Language, String> {
    Language save(Language language);
}