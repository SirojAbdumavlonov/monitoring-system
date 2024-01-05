package com.example.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.department.entity.Department;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    Optional<Department> findById(String id);
    List<Department> findAll();

    Optional<Department> findByDepartmentName(String departmentName);

    List<Department> findByIdOfMainBranch(String departmentId);

    boolean existsByIdOfMainBranchAndId(String idOfMainBranch, String subBranchId);

    boolean existsById(String id);
    @Query(
            "SELECT d.id FROM Department d WHERE d.idOfMainBranch = ?1"
    )
    List<String> findAllByIdOfMainBranch(String departmentId);


}
