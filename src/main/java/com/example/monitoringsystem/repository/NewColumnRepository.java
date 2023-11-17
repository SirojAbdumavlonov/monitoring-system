package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.NewColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewColumnRepository extends MongoRepository<NewColumn,String> {
    @Query(
            "SELECT n.name FROM NewColumn n WHERE n.department = ?1"
    )
    public List<String> findNamesOfColumns(String departmentId);

    Optional<NewColumn> findByNameAndDepartmentId(String name, String departmentId);

    public List<NewColumn> findByDepartmentId(String departmentId);

    public NewColumn findByNameAndDepartment_Id(String name, String departmentId);
}
