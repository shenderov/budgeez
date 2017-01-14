package com.kamabizbazti.security.repository;

import com.kamabizbazti.model.entities.Currency;
import com.kamabizbazti.model.entities.Language;
import com.kamabizbazti.security.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    List<User> findAll();

    User save(User user);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO user_authorities(users_id, authorities_id) VALUES (?2, ?1)")
    void grantAuthority(long authorityId, long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM user_authorities WHERE users_id = ?2 AND authorities_id = ?1")
    void detachAuthority(long authorityId, long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE user SET currency_code = ?1 WHERE id = ?2")
    void updateUserCurrency(Currency currency, long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE user SET language_code = ?1 WHERE id = ?2")
    void updateUserLanguage(Language language, long userId);
}
