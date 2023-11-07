package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.entity.ExactColumnsDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel="spring")
public interface ExactColumnsMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateChangedColumn(ExactColumnsDTO exactColumnsDTO, @MappingTarget ExactColumns exactColumns);
}
