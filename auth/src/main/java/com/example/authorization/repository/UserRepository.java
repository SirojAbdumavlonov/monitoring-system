package com.example.authorization.repository;

import com.example.authorization.entity.Userr;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Userr, String> {
    Optional<Userr> findByUserId(String id);

    boolean existsByUserId(String id);

}
