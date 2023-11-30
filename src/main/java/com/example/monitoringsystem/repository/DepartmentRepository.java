package com.example.monitoringsystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.monitoringsystem.entity.Department;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {

    List<Department> findAll();


//    @Query("{'id': ?0, 'address': ?1}")
//    List<Department> findDepartmentsByAddress(String id, String address2);

    Optional<Department> findByDepartmentName(String departmentName);

    List<Department> findByIdOfMainBranch(String departmentId);

    boolean existsByIdOfMainBranchAndId(String idOfMainBranch, String subBranchId);

    boolean existsById(String id);

    @Query(
            fields = "{'id': 0}",value = "{'idOfMainBranch': ?0}"
    )
    List<String> findAllByIdOfMainBranch(String idOfMainBranch);

    Department findDepartmentById(String id);

}
