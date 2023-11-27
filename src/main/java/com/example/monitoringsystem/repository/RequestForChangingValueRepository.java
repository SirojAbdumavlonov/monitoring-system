package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.RequestForFixedValue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestForChangingValueRepository extends MongoRepository<RequestForFixedValue, String> {
    List<RequestForFixedValue> findByStatus(String status);

    List<RequestForFixedValue> findAllByOrderByCreatedDateTimeDesc();
}
