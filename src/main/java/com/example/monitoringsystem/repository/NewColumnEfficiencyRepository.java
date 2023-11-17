package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.NewColumnEfficiency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewColumnEfficiencyRepository extends MongoRepository<NewColumnEfficiency, String> {
}
