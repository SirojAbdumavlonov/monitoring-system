package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.model.ReasonOfDeclining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReasonOfDecliningRepository extends MongoRepository<ReasonOfDeclining, String> {
}
