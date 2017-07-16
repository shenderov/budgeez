package com.budgeez.model.repository;

import com.budgeez.model.entities.dao.User;
import com.budgeez.security.entities.ActivationToken;
import com.budgeez.security.entities.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivationTokenRepository  extends CrudRepository<ActivationToken, Long> {

    ActivationToken findByUser(User user);

    ActivationToken findByToken(Token token);
}
