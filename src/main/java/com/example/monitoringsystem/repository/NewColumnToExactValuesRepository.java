package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.NewColumnsToExactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewColumnToExactValuesRepository extends MongoRepository<NewColumnsToExactValue,String> {
    List<NewColumnsToExactValue> findByDepartmentId(String departmentId);
}
