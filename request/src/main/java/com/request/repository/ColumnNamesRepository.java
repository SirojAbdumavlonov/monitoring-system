package com.request.repository;

import com.request.entity.ColumnNames;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ColumnNamesRepository extends MongoRepository<ColumnNames, String> {

    List<ColumnNames> findAll();
}
