package com.kamabizbazti.security.repository;

import com.kamabizbazti.security.entities.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    Authority findOne(Long id);
    Authority save(Authority authority);
}
