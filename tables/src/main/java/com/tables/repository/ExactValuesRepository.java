package com.tables.repository;

import com.tables.entity.ExactValues;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExactValuesRepository extends MongoRepository<ExactValues, String> {

    ExactValues findByDepartmentId(String departmentId);
}
