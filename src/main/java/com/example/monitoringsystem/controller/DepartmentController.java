package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.repository.ExactColumnsRepository;
import com.example.monitoringsystem.security.CurrentUserId;
import com.example.monitoringsystem.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.monitoringsystem.model.NewDepartment;


import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final ExactColumnsService exactColumnsService;

    @GetMapping("/")
    public ResponseEntity<?> getAllDepartments(){

        List<Department> allDepartments = departmentService.getAllDepartmentsData();

        return ResponseEntity.ok(allDepartments);
    }
    @PostMapping("/add-department")
    public ResponseEntity<?> addingDepartment(@Valid @RequestBody NewDepartment newDepartment){

        departmentService.saveNewDepartment(newDepartment);

        return ResponseEntity.ok("Saved successfully!");
    }
    @GetMapping("/add-info")
    public ResponseEntity<?> getAllData(@CurrentUserId String userId) {

        Long id = Long.valueOf(userId);

        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN") || a.getAuthority().equals("USER"))) {
            return ResponseEntity.ok(departmentService.findDepartmentOfUser(id));
        }
        else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("SUPER_ADMIN"))) {
            return ResponseEntity.ok(departmentService.findAllDepartments());
        }
        return ResponseEntity.ok("Not found!");
    }
}
