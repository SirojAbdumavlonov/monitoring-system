package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.TemporaryUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemporaryUserDetailsRepository extends MongoRepository<TemporaryUserDetails, String> {


}
