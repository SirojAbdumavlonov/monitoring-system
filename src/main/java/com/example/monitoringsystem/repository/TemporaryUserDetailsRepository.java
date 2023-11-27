package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.TemporaryUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TemporaryUserDetailsRepository extends MongoRepository<TemporaryUserDetails, String> {


}
