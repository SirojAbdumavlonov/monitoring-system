package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/")
    public ResponseEntity<?> getAllDepartments(){

        List<Department> allDepartments = departmentService.getAllDepartmentsData();

        return ResponseEntity.ok(allDepartments);
    }
}
