package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Userr;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Userr, String> {
    Optional<Userr> findByUserId(String id);

    boolean existsByUserId(String id);

    List<Userr> findByUserIdContainingIgnoreCase(String userId);
    List<Userr> findByFullNameContainingIgnoreCase(String fullName);


}
