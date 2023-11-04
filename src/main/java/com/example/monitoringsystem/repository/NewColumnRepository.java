package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.NewColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewColumnRepository extends JpaRepository<NewColumn,Long> {
    @Query(
            "SELECT n.name FROM NewColumn n WHERE n.department = ?1"
    )
    public List<String> findNamesOfColumns(Long departmentId);

    public Optional<NewColumn> findByName(String name);

    public List<NewColumn> findByDepartmentId(Long departmentId);
}
