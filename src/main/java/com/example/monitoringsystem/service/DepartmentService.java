package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartmentsData(){
        return departmentRepository.findAllDepartments();
    }

}
