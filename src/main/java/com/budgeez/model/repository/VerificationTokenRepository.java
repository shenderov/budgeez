package com.budgeez.model.repository;

import com.budgeez.security.entities.Token;
import com.budgeez.security.entities.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository  extends CrudRepository<VerificationToken, Long> {

    VerificationToken findByUserUuid(String uuid);

    VerificationToken findByToken(Token token);
}
