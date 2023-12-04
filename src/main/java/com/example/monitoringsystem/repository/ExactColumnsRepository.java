package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.ExactColumns;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExactColumnsRepository extends MongoRepository<ExactColumns,String> {


    Optional<ExactColumns> findByCreatedDateAndDepartmentId(LocalDate date, String departmentId);

    List<ExactColumns> findAllByDepartmentIdInAndCreatedDateBetweenOrderByCreatedDateDescDepartmentId
            (List<String> ids, LocalDate from, LocalDate to);
    List<ExactColumns> findAllByDepartmentIdAndCreatedDateBetweenOrderByCreatedDateDesc
            (String departmentId, LocalDate from, LocalDate to);
    List<ExactColumns> findAllByCreatedDateBetweenOrderByCreatedDateDescDepartmentId
            (LocalDate from, LocalDate to);

}
