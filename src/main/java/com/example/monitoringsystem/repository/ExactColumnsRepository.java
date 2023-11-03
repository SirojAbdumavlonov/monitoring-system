package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.entity.ExactColumns;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExactColumnsRepository extends JpaRepository<ExactColumns,Long> {


//    @Query(
//            "SELECT e from ExactColumns e where e.id = ?1 "
//    )
//    Optional<ExactColumns> findByDepartmentId(Long departmentId);

    Optional<ExactColumns> findByDepartmentId_Id(Long departmentId);

}
