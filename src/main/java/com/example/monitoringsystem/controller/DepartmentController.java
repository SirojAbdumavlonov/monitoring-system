package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.monitoringsystem.model.NewDepartment;


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
    @PostMapping("/add-department")
    public ResponseEntity<?> addingDepartment(@Valid @RequestBody NewDepartment newDepartment){

        departmentService.saveNewDepartment(newDepartment);

        return null;
    }
}
