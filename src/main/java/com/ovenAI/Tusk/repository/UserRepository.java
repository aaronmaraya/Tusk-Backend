package com.ovenAI.Tusk.repository;

import com.ovenAI.Tusk.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
