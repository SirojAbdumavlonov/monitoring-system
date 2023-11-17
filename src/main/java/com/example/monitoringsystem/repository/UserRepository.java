package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Userr;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Userr, String> {
    @Override
    Optional<Userr> findById(String id);


}
