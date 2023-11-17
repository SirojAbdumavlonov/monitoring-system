package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, String> {

}
