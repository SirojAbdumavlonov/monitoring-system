package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
