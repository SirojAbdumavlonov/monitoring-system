package com.example.monitoringsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.monitoringsystem.entity.Department;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(
            "SELECT d FROM Department d"
    )
    List<Department> findAllDepartments();

    @Override
    List<Department> findAll();

    List<Department> findByAddress(String address);


            @Query(
                    "SELECT d FROM Department d WHERE d.address = ?2 AND d.id = ?1"
            )
    List<Department> findDepartmentsByAddress(Long id, String address2);

            Optional<Department> findByDepartmentName(String departmentName);
}
