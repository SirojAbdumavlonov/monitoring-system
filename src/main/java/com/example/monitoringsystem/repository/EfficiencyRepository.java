package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Efficiency;
import com.example.monitoringsystem.model.ValueWithEfficiency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Repository
public interface EfficiencyRepository extends MongoRepository<Efficiency, String> {


    List<Efficiency> findAllByDepartmentIdInAndCreatedDateBetweenOrderByCreatedDateDescDepartmentId
            (List<String> departmentId, LocalDate from, LocalDate to);
    List<Efficiency> findAllByCreatedDateBetweenOrderByCreatedDateDescDepartmentId
            (LocalDate from, LocalDate to);
    List<Efficiency> findAllByDepartmentIdAndCreatedDateBetweenOrderByCreatedDateDesc
            (String departmentId, LocalDate from, LocalDate to);

//    @Query(
//            "SELECT NEW com.example.monitoringsystem.model.ValueWithEfficiency(ef, ex) FROM Efficiency ef, ExactColumns ex " +
//                    "WHERE ef.departmentId = ?1 AND ef.createdDate = ex.createdDate " +
//                    "AND ef.createdDate BETWEEN ?2 AND ?3 " +
//                    "ORDER BY ef.createdDate DESC"
//    )



}
