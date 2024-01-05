package com.request.repository;

import com.request.entity.RequestForFixedValue;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestForChangingValueRepository extends MongoRepository<RequestForFixedValue, String> {
    List<RequestForFixedValue> findByStatusOrderByLocalDateTimeDesc(String status);

    List<RequestForFixedValue> findAllByOrderByLocalDateTimeDesc();
}
