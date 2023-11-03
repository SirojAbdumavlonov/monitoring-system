package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.NewColumnsToExactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewColumnToExactValuesRepository extends JpaRepository<NewColumnsToExactValue,Long> {
}
