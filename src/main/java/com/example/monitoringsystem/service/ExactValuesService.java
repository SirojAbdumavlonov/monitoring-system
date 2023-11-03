package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.AllColumns;
import com.example.monitoringsystem.model.NewColumnModel;
import com.example.monitoringsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExactValuesService {
    private final ExactValuesRepository exactValuesRepository;
    private final ExactColumnsRepository exactColumnsRepository;
    private final NewColumnRepository newColumnRepository;
    private final DepartmentRepository departmentRepository;
    private final NewColumnToExactValuesRepository newColumnToExactValuesRepository;

    public void saveData(AllColumns allColumns) {

        Department department =
                departmentRepository.getReferenceById(allColumns.getDepartmentId());

        List<NewColumnsToExactValue> newColumns = new ArrayList<>();
        if(allColumns.getNewColumns() != null){
            for (NewColumnModel model : allColumns.getNewColumns()) {
                NewColumnsToExactValue newColumn = NewColumnsToExactValue
                        .builder()
                        .value(model.getValue())
                        .name(model.getColumnName())
                        .departmentId(department)
                        .build();
                newColumnToExactValuesRepository.save(newColumn);
            }
        }

        ExactValues exactValues = ExactValues
                .builder()
                .bankomats(allColumns.getBankomats())
                .computers(allColumns.getComputers())
                .employees(allColumns.getEmployees())
                .monitor(allColumns.getMonitor())
                .mouse(allColumns.getMouse())
                .printer(allColumns.getPrinter())
                .build();
        exactValuesRepository.save(exactValues);
    }

    public void saveDailyData(AllColumns allColumns) {

        Department department =
                departmentRepository.getReferenceById(allColumns.getDepartmentId());

        List<NewColumn> newColumns = new ArrayList<>();

        for (NewColumnModel model : allColumns.getNewColumns()) {
            //Firstly, I check if I have a column with this name
            if(newColumnRepository.findByName(model.getColumnName()).isPresent()){
                //Then, I find out and initiate it to its class
                NewColumn newColumn = newColumnRepository.findByName
                        (model.getColumnName()).orElseThrow(()
                        -> new BadRequestException("Error in getting column"));
                //Get all values of column which name is in given class
                List<String> values = newColumn.getValues();
                //Add to table new value to new/old column
                values.add(model.getValue());
                //Change values of column to old list + new value
                newColumn.setValues(values);
                //Save column with new value
                newColumnRepository.save(newColumn);
            }
            else {
                NewColumn newColumn = NewColumn.builder()
                        .values(List.of(model.getValue()))
                        .name(model.getColumnName())
                        .department(department)
                        .build();
                newColumnRepository.save(newColumn);
            }
        }

        ExactColumns exactColumns = ExactColumns
                .builder()
                .bankomats(allColumns.getBankomats())
                .computers(allColumns.getComputers())
                .employees(allColumns.getEmployees())
                .monitor(allColumns.getMonitor())
                .mouse(allColumns.getMouse())
                .printer(allColumns.getPrinter())
                .department(department)
                .build();
        exactColumnsRepository.save(exactColumns);
    }





}
