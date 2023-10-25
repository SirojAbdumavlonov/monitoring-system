package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.RequestOfUser;
import com.example.monitoringsystem.entity.TemporaryUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryUserDetailsRepository extends JpaRepository<TemporaryUserDetails, Long> {

}
