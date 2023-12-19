package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.entity.ExactValues;
import com.example.monitoringsystem.model.DepartmentDTO;
import com.example.monitoringsystem.model.ExactColumnsDTO;
import com.example.monitoringsystem.model.ExactValuesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ColumnsMapperImpl implements ColumnsMapper{
    @Override
    public void updateChangedColumn(ExactColumnsDTO exactColumnsDTO, ExactColumns exactColumns) {
        log.debug("Before mapping: exactColumnsDTO = {}, exactColumns = {}", exactColumnsDTO, exactColumns);

        // Perform the mapping
        // ...

        log.debug("After mapping: exactColumnsDTO = {}, exactColumns = {}", exactColumnsDTO, exactColumns);

    }

    @Override
    public void updateChangedFixedColumn(ExactValuesDTO exactValuesDTO, ExactValues exactValues) {
        log.debug("Before mapping: exactColumnsDTO = {}, exactColumns = {}", exactValuesDTO, exactValues);

        // Perform the mapping
        // ...

        log.debug("After mapping: exactColumnsDTO = {}, exactColumns = {}", exactValuesDTO, exactValues);

    }

    @Override
    public void updateChangedDepartmentColumn(DepartmentDTO departmentDTO, Department department) {
        log.debug("Before mapping: exactColumnsDTO = {}, exactColumns = {}", departmentDTO, department);

        // Perform the mapping
        // ...

        log.debug("After mapping: exactColumnsDTO = {}, exactColumns = {}", departmentDTO, department);

    }
}
