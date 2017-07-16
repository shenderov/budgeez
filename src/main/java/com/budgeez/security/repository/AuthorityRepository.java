package com.budgeez.security.repository;

import com.budgeez.security.entities.Authority;
import com.budgeez.security.entities.AuthorityName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    Authority findOne(Long id);

    Authority findByName(AuthorityName name);

    Authority save(Authority authority);
}
