package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.ColumnNames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnNamesRepository extends JpaRepository<ColumnNames, Long> {
    @Query(
            "SELECT c.columnName FROM ColumnNames c"
    )
    List<String> findAllColumns();

    boolean existsByColumnName(String columnName);
}
