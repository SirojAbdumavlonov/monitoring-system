package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.entity.NewColumn;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.payload.ColumnNames;
import com.example.monitoringsystem.repository.ColumnNamesRepository;
import com.example.monitoringsystem.repository.ExactColumnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExactColumnsService {
    private final ExactColumnsRepository exactColumnsRepository;
    private final DepartmentService departmentService;
    private final ColumnNamesRepository columnNamesRepository;


    public List<ExactColumns> getPreviousDaysData(String userId) {
        //1. Find department id where he/she works - done
        //2. Use department id to find table(exactColumns) in which data is saved
        //3. Write a query to find this data(use IN keyword in sql)
        //4. Add it for method in service and ,eventually, to controller

        String departmentId = departmentService.findDepartmentOfUser(userId).getId();

        return exactColumnsRepository.findAllByDepartmentId(departmentId);
    }
    public ColumnNames getNamesOfColumns(String userId){

        String departmentId = departmentService.findDepartmentOfUser(userId).getId();

        List<String> namesOfColumnOfDefaultTable =
                columnNamesRepository.findAllColumnNames();

        ExactColumns exactColumns =
                exactColumnsRepository.findByDepartmentId(departmentId)
                        .orElseThrow(() -> new BadRequestException("Columns not found!"));
        if(!exactColumns.getNewColumns().isEmpty()) {
            List<String> namesOfNewColumns = new ArrayList<>();
            for (NewColumn newColumn : exactColumns.getNewColumns()) {
                namesOfNewColumns.add(
                        newColumn.getName()
                );
            }
            namesOfColumnOfDefaultTable.addAll(namesOfNewColumns);
        }
        return new ColumnNames(namesOfColumnOfDefaultTable);
    }
    public ExactColumns getTodayDailyFilledData(LocalDate today, String userId){

        String departmentId = departmentService.findDepartmentOfUser(userId).getId();

        return exactColumnsRepository.findByCreatedDateAndDepartmentId(today, departmentId)
                .orElseThrow(() -> new BadRequestException("Not found table with data!"));
    }

}
