package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.ExactValues;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExactValuesRepository extends MongoRepository<ExactValues, String> {

    ExactValues findByDepartmentId(String departmentId);
}
