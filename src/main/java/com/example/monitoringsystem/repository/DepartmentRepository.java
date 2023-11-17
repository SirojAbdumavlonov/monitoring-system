package com.example.monitoringsystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.monitoringsystem.entity.Department;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {



    List<Department> findAll();

    List<Department> findByAddress(String address);

//    @Query("{'id': ?0, 'address': ?1}")
//    List<Department> findDepartmentsByAddress(String id, String address2);

    Optional<Department> findByDepartmentName(String departmentName);

    List<Department> findByIdOfMainBranch(String departmentId);

    boolean existsByIdOfMainBranch(String departmentId);

}
