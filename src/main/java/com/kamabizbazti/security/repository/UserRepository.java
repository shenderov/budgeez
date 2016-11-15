package com.kamabizbazti.security.repository;

import com.kamabizbazti.security.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    List<User> findAll();

    User save(User user);
}
