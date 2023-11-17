package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Efficiency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EfficiencyRepository extends MongoRepository<Efficiency, String> {
}
