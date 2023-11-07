package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.HistoryOfChanges;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryOfChangesRepository extends JpaRepository<HistoryOfChanges, Long> {
}
