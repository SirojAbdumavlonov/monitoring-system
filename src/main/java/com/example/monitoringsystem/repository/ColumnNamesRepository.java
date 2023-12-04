package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.ColumnNames;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ColumnNamesRepository extends MongoRepository<ColumnNames, String> {

    boolean existsByColumnName(String columnName);
}
