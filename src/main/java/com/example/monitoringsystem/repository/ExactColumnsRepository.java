package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.ExactColumns;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExactColumnsRepository extends MongoRepository<ExactColumns,String> {


//    @Query(
//            "SELECT e from ExactColumns e where e.id = ?1 "
//    )
//    Optional<ExactColumns> findByDepartmentId(Long departmentId);

    List<ExactColumns> findAllByDepartmentId(String departmentId);
    Optional<ExactColumns> findByDepartmentId(String departmentId);


    Optional<ExactColumns> findByCreatedDateAndDepartmentId(LocalDate date, String departmentId);

    ExactColumns findByCreatedDate(LocalDate date);

    List<ExactColumns> findAllByDepartmentIdInAndCreatedDateBetweenOrderByCreatedDateDescDepartmentId
            (List<String> ids, LocalDate from, LocalDate to);
    List<ExactColumns> findAllByDepartmentIdAndCreatedDateBetweenOrderByCreatedDateDesc
            (String departmentId, LocalDate from, LocalDate to);
    List<ExactColumns> findAllByCreatedDateBetweenOrderByCreatedDateDescDepartmentId
            (LocalDate from, LocalDate to);

}
