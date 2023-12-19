package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.entity.ExactValues;
import com.example.monitoringsystem.model.DepartmentDTO;
import com.example.monitoringsystem.model.ExactColumnsDTO;
import com.example.monitoringsystem.model.ExactValuesDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ColumnsMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateChangedColumn(ExactColumnsDTO exactColumnsDTO, @MappingTarget ExactColumns exactColumns);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateChangedFixedColumn(ExactValuesDTO exactValuesDTO, @MappingTarget ExactValues exactValues);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateChangedDepartmentColumn(DepartmentDTO departmentDTO, @MappingTarget Department department);
}
