package com.budgeez.security.repository;

import com.budgeez.model.entities.dao.User;
import com.budgeez.security.entities.PasswordResetToken;
import com.budgeez.security.entities.Token;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(Token token);

    PasswordResetToken findByUser(User user);

    @Modifying
    @Transactional
    void deleteByToken(Token token);

    @Modifying
    @Transactional
    void deleteByUser(User user);

}
