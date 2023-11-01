package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.ExactValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExactValuesRepository extends JpaRepository<ExactValues, Long> {


}
