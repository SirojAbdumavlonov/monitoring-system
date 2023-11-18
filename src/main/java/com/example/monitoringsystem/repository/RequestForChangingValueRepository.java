package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.RequestForFixedValue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequestForChangingValueRepository extends MongoRepository<RequestForFixedValue, String> {
    List<RequestForFixedValue> findByStatus(String status);

    List<RequestForFixedValue> findAllByOrderByCreatedDateTimeDesc();
}
