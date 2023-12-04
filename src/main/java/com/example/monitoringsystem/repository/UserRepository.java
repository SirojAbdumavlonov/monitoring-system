package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Userr;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Userr, String> {
    @Override
    Optional<Userr> findById(String id);

    boolean existsById(String id);
}
