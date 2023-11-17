package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.HistoryOfChanges;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryOfChangesRepository extends MongoRepository<HistoryOfChanges, String> {
}
