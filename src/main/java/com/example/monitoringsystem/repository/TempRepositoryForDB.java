package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.RequestOfUser;
import com.example.monitoringsystem.entity.TemporaryUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempRepositoryForDB extends JpaRepository<RequestOfUser,Long> {

}
