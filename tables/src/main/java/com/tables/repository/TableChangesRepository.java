package com.tables.repository;

import com.tables.entity.TableChanges;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TableChangesRepository extends MongoRepository<TableChanges, String> {
    List<TableChanges> findAllByDepartmentIdAndCreatedDateBetweenOrderByCreatedDateDesc
            (String departmentId, LocalDate from, LocalDate to);
}
