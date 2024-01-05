package com.tables.repository;

import com.tables.entity.ExactColumns;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExactColumnsRepository extends MongoRepository<ExactColumns,String> {


    Optional<ExactColumns> findByDateAndDepartmentId(LocalDate date, String departmentId);

    List<ExactColumns> findAllByDepartmentIdInAndDateBetweenOrderByDateDescDepartmentId
            (List<String> ids, LocalDate from, LocalDate to);
    List<ExactColumns> findAllByDepartmentIdAndDateBetweenOrderByDateDesc
            (String departmentId, LocalDate from, LocalDate to);
    List<ExactColumns> findAllByDateBetweenOrderByDateDescDepartmentId
            (LocalDate from, LocalDate to);
    boolean existsByDate(LocalDate createdDate);

}
