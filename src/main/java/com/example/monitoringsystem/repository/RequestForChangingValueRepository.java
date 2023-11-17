package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.RequestForFixedValue;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestForChangingValueRepository extends MongoRepository<RequestForFixedValue, String> {
}
