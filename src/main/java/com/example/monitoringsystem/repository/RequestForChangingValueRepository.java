package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestForChangingValueRepository extends MongoRepository<Request, String> {
    List<Request> findByStatusOrderByLocalDateTimeDesc(String status);

    List<Request> findAllByOrderByLocalDateTimeDesc();
}
