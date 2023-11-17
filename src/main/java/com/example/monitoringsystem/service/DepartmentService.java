package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.entity.Location;
import com.example.monitoringsystem.entity.Userr;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.NewDepartment;
import com.example.monitoringsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;


    public void saveNewDepartment(NewDepartment newDepartment) {
        Location location = Location.builder()
                .lon(newDepartment.getLon())
                .lat(newDepartment.getLat())
                .build();

        Department department = Department.builder()
                .address(newDepartment.getAddress())
                .departmentName(newDepartment.getDepartmentName())
                .location(location)
                .idOfMainBranch(newDepartment.getIdOfMainBranch())
                .id(newDepartment.getId())
                .build();

        locationRepository.save(location);
        departmentRepository.save(department);
    }

    public Department findDepartmentOfUser(String userId) {
        Userr found = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Not found!"));
        return found.getDepartment();
    }

    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }
    public List<Department> findDepartmentWithItsSubBranches(String userId){
        Userr found = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Not found!"));
        String departmentId = found.getDepartment().getId();
        List<Department> getAllDepartments = new java.util.ArrayList<>(List.of(found.getDepartment()));

        getAllDepartments.addAll(departmentRepository.findByIdOfMainBranch(departmentId));

        return getAllDepartments;
    }
    public Department findDepartmentByItsIdForSuperAdmin(String departmentId){//this method is for super admin
        return departmentRepository.findById(departmentId).
                orElseThrow(() -> new BadRequestException("THere is no department with this id"));
    }
    public Department findDepartmentByIdForAdmin(String departmentID, String adminId){
        Userr found = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Not found!"));
        String departmentId = found.getDepartment().getId();

        if(departmentRepository.existsByIdOfMainBranch(departmentId)){
            //firstly, it checks if his branch is the main
            return departmentRepository.findById(departmentID).
                    orElseThrow(() -> new BadRequestException("THere is no department with this id"));
        }
        throw  new BadRequestException("You don't have an access to see this department's data!");
    }



}
