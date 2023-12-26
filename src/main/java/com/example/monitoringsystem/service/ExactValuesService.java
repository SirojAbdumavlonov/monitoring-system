package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.*;
import com.example.monitoringsystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExactValuesService {
    private final ExactValuesRepository exactValuesRepository;
    private final ColumnNamesRepository columnNamesRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void saveData(AllColumns allColumns, String departmentId) {

        if(!departmentRepository.existsById(departmentId)){
            throw new BadRequestException("Department not found!");
        }

        List<NewColumnsToExactValue> newColumns = new ArrayList<>();

        if(allColumns.newColumns() != null){
            for (NewColumnModel model : allColumns.newColumns()) {
                NewColumnsToExactValue newColumn = NewColumnsToExactValue
                        .builder()
                        .value(model.value())
                        .name(model.columnName())
                        .build();
                newColumns.add(newColumn);
                if (!columnNamesRepository.existsByColumnName(model.columnName())) {
                    columnNamesRepository.save(new ColumnNames(model.columnName()));
                }
            }
        }

        ExactValues exactValues = ExactValues
                .builder()
                .bankomats(allColumns.bankomats())
                .computers(allColumns.computers())
                .employees(allColumns.employees())
                .monitor(allColumns.monitor())
                .mouse(allColumns.mouse())
                .printer(allColumns.printer())
                .newColumnsToExactValueList(newColumns)
                .departmentId(departmentId)
                .build();
        exactValuesRepository.save(exactValues);
    }

    //            //Firstly, I check if I have a column with this name
//            if(newColumnRepository.findByNameAndDepartmentId(model.getColumnName(),department.getId()).isPresent()){
//                //Then, I find out and initiate it to its class
//                NewColumn newColumn = newColumnRepository.findByNameAndDepartmentId
//                        (model.getColumnName(),department.getId()).orElseThrow(()
//                        -> new BadRequestException("Error in getting column"));
//                //Get all values of column which name is in given class
//                List<ValueWithDate> values = newColumn.getValues();
//                //Add to table new value to new/old column
//                values.add(new ValueWithDate(model.getValue(), today));
//                //Change values of column to old list + new value
//                newColumn.setValues(values);
//                //Save column with new value
//                newColumnRepository.save(newColumn);



}
