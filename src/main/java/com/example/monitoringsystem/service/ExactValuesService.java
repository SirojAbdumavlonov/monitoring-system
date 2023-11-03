package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.model.AllColumns;
import com.example.monitoringsystem.model.NewColumnModel;
import com.example.monitoringsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExactValuesService {
    private final ExactValuesRepository exactValuesRepository;
    private final ExactColumnsRepository exactColumnsRepository;
    private final NewColumnsRepository newColumnsRepository;
    private final DepartmentRepository departmentRepository;
    private final NewColumnsToExactValuesRepository newColumnsToExactValuesRepository;

    public void saveData(AllColumns allColumns) {

        Department department =
                departmentRepository.getReferenceById(allColumns.getDepartmentId());

        List<NewColumnsToExactValue> newColumns = new ArrayList<>();

        for (NewColumnModel model : allColumns.getNewColumns()) {
            NewColumnsToExactValue newColumn = NewColumnsToExactValue
                    .builder()
                    .value(model.getValue())
                    .name(model.getColumnName())
                    .departmentId(department)
                    .build();
            newColumnsToExactValuesRepository.save(newColumn);

            newColumns.add(newColumn);
        }

        ExactValues exactValues = ExactValues
                .builder()
                .bankomats(allColumns.getBankomats())
                .computers(allColumns.getComputers())
                .employees(allColumns.getEmployees())
                .monitor(allColumns.getMonitor())
                .mouse(allColumns.getMouse())
                .printer(allColumns.getPrinter())
                .newColumns(newColumns)
                .build();

        exactValuesRepository.save(exactValues);

    }


    public void saveDailyData(AllColumns allColumns) {

        Department department =
                departmentRepository.getReferenceById(allColumns.getDepartmentId());

        List<NewColumn> newColumns = new ArrayList<>();

        for (NewColumnModel model : allColumns.getNewColumns()) {
            NewColumn newColumn = NewColumn.builder()
                    .value(model.getValue())
                    .name(model.getColumnName())
                    .id(department)
                    .build();
            newColumnsRepository.save(newColumn);

            newColumns.add(newColumn);
        }


        ExactColumns exactColumns = ExactColumns
                .builder()
                .bankomats(allColumns.getBankomats())
                .computers(allColumns.getComputers())
                .employees(allColumns.getEmployees())
                .monitor(allColumns.getMonitor())
                .mouse(allColumns.getMouse())
                .printer(allColumns.getPrinter())
                .newColumns(newColumns)
                .build();
        exactColumnsRepository.save(exactColumns);
    }





}
