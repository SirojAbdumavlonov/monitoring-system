package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.ExactColumns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExactColumnsRepository extends JpaRepository<ExactColumns,Long> {

}
