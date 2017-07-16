package com.budgeez.security.repository;

import com.budgeez.model.entities.dao.Currency;
import com.budgeez.model.entities.dao.Language;
import com.budgeez.model.entities.dao.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findByUuid(String uuid);

    List<User> findAll();

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

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE user SET username = ?1 WHERE id = ?2")
    void updateEmail(String email, long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE user SET password = ?1 WHERE id = ?2")
    void updatePassword(String password, long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE user SET enabled = ?1 WHERE id = ?2")
    void uptageEnabled(boolean enabled, long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE user SET name = ?1, language_code = ?2, currency_code = ?3, start_day = ?4 WHERE username = ?5")
    void updateUserDetails(String name, Language language, Currency currency, Integer startDay, String userName);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE user SET is_activated = TRUE, status = 'ACTIVE' WHERE id = ?1")
    void activateUser(long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE user SET enabled = FALSE, status = 'DISABLED' WHERE id = ?1")
    void disableUser(long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE user SET enabled = TRUE , status = 'ACTIVE' WHERE username = ?1")
    void reviveUser(String username);
}
