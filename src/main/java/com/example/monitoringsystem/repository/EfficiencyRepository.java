package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Efficiency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface EfficiencyRepository extends MongoRepository<Efficiency, String> {


    List<Efficiency> findAllByDepartmentIdInAndCreatedDateBetweenOrderByCreatedDateDescDepartmentId
            (List<String> departmentId, LocalDate from, LocalDate to);
    List<Efficiency> findAllByCreatedDateBetweenOrderByCreatedDateDescDepartmentId
            (LocalDate from, LocalDate to);
    List<Efficiency> findAllByDepartmentIdAndCreatedDateBetweenOrderByCreatedDateDesc
            (String departmentId, LocalDate from, LocalDate to);



}
