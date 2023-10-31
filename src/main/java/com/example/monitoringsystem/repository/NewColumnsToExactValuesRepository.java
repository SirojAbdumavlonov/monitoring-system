package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.NewColumnsToExactValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewColumnsToExactValuesRepository extends JpaRepository<NewColumnsToExactValue,Long> {
}
