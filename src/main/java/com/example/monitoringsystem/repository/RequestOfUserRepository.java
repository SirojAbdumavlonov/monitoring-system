package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.RequestOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestOfUserRepository extends JpaRepository<RequestOfUser,Long> {
    public List<RequestOfUser> findByStatus(String status);
    @Query(
            "UPDATE RequestOfUser r SET r.status = ?1 WHERE r.requestId = ?2"
    )
    public void updateRequestOfStatus(String status, Long id);

}
