package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.repository.ExactColumnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ExactColumnsService {
    private final ExactValuesService exactValuesService;
    private final ExactColumnsRepository exactColumnsRepository;
    private final DepartmentService departmentService;


    public ExactColumns getDailyData(Long userId) {
        //1. Find department id where he/she works - done
        //2. Use department id to find table(exactColumns) in which data is saved
        //3. Write a query to find this data(use IN keyword in sql)
        //4. Add it for method in service and ,eventually, to controller

        Long departmentId = departmentService.findDepartmentOfUser(userId).getId();

        ExactColumns exactColumns = exactColumnsRepository.findByDepartmentId_Id(departmentId)
                .orElseThrow(() -> new RuntimeException(" Data is not found!"));


        return exactColumns;


    }

}
