package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.ExactColumns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExactColumnsRepository extends JpaRepository<ExactColumns,Long> {


//    @Query(
//            "SELECT e from ExactColumns e where e.id = ?1 "
//    )
//    Optional<ExactColumns> findByDepartmentId(Long departmentId);

    List<ExactColumns> findByDepartmentId_Id(Long departmentId);

    ExactColumns findByCreatedDateAndDepartmentId(LocalDate date, Long departmentId);

    ExactColumns findByCreatedDate(LocalDate date);

}
