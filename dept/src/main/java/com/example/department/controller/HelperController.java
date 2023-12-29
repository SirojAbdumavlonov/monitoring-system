package com.example.department.controller;

import com.example.department.entity.Department;
import com.example.department.service.DepartmentService;
import com.util.model.UpdatableDepartment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HelperController {
    private final DepartmentService departmentService;
    @GetMapping("/dept-name/{departmentName}")
    public String getDepartmentIdByName(@PathVariable String departmentName){
        return departmentService.getDepartmentByName(departmentName).getId();
    }
    @GetMapping("/dept-id/{departmentId}")
    public Department getDepartmentById(@PathVariable String departmentId){
        return departmentService.getByDepartmentId(departmentId);
    }
    @GetMapping("/check-dept/{departmentId}")
    public Boolean departmentExistsById(@PathVariable String departmentId){
        return departmentService.existsById(departmentId);
    }
    @PostMapping("/update-save-department")
    public void updateAndSaveDepartment(@RequestBody UpdatableDepartment department){
        departmentService.saveAndUpdate(department.departmentId(), department.departmentDTO());
    }
}
