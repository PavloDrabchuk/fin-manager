package com.example.personalfinancemanager.repository;

import com.example.personalfinancemanager.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findFirstByUsername(String username);
}
