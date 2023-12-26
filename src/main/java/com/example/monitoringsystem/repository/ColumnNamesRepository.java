package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.ColumnNames;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ColumnNamesRepository extends MongoRepository<ColumnNames, String> {

    boolean existsByColumnName(String columnName);
    ColumnNames findByColumnName(String columnName);
    List<ColumnNames> findAll();
}
