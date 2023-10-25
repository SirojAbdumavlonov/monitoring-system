package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.RequestOfUser;
import com.example.monitoringsystem.entity.TemporaryUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TemporaryUserDetailsRepository extends JpaRepository<TemporaryUserDetails, Long> {


}
