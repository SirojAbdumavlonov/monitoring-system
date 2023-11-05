package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.NewColumnEfficiency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewColumnEfficiencyRepository extends JpaRepository<NewColumnEfficiency, Long> {
}
