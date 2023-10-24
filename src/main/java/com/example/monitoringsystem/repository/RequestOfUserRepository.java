package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.RequestOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestOfUserRepository extends JpaRepository<RequestOfUser,Long> {

}
