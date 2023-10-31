package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.NewColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewColumnsRepository extends JpaRepository<NewColumn,Long> {

}
