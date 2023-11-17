package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.payload.ColumnNames;
import com.example.monitoringsystem.repository.ColumnNamesRepository;
import com.example.monitoringsystem.repository.ExactColumnsRepository;
import com.example.monitoringsystem.repository.NewColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExactColumnsService {
    private final ExactValuesService exactValuesService;
    private final ExactColumnsRepository exactColumnsRepository;
    private final DepartmentService departmentService;
    private final ColumnNamesRepository columnNamesRepository;
    private final NewColumnRepository newColumnRepository;


    public List<ExactColumns> getPreviousDaysData(String userId) {
        //1. Find department id where he/she works - done
        //2. Use department id to find table(exactColumns) in which data is saved
        //3. Write a query to find this data(use IN keyword in sql)
        //4. Add it for method in service and ,eventually, to controller

        String departmentId = departmentService.findDepartmentOfUser(userId).getId();

        return exactColumnsRepository.findByDepartmentId_Id(departmentId);
    }
    public ColumnNames getNamesOfColumns(String userId){

        String departmentId = departmentService.findDepartmentOfUser(userId).getId();

        List<String> namesOfColumnOfDefaultTable =
                columnNamesRepository.findAllColumnNames();

        List<String> namesOfNewColumns =
                newColumnRepository.findNamesOfColumns(departmentId);

        namesOfColumnOfDefaultTable.addAll(namesOfNewColumns);

        return new ColumnNames(namesOfColumnOfDefaultTable);
    }
    public ExactColumns getTodayDailyFilledData(LocalDate today, String userId){

        String departmentId = departmentService.findDepartmentOfUser(userId).getId();

        return exactColumnsRepository.findByCreatedDateAndDepartmentId(today, departmentId);
    }

}
