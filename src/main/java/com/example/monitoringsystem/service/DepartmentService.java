package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.entity.Location;
import com.example.monitoringsystem.entity.RequestForFixedValue;
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
    private final UserRepository userRepository;
    private final RequestForChangingValueRepository forChangingValueRepository;


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


        departmentRepository.save(department);
    }
    private Department getDepartmentById(String departmentId){
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BadRequestException("Department not found"));
    }

    public Department findDepartmentOfUser(String userId) {
        Userr found = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Not found!"));

        return getDepartmentById(found.getDepartmentId());
    }

    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }
    public List<Department> findDepartmentWithItsSubBranches(String userId){
        Userr found = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Not found!"));

        String departmentId = found.getDepartmentId();

        Department department = getDepartmentById(departmentId);


        List<Department> getAllDepartments = new java.util.ArrayList<>(List.of(department));

        getAllDepartments.addAll(departmentRepository.findByIdOfMainBranch(departmentId));

        return getAllDepartments;
    }
    public Department findDepartmentByItsIdForSuperAdmin(String departmentId){//this method is for super admin
        return departmentRepository.findById(departmentId).
                orElseThrow(() -> new BadRequestException("THere is no department with this id"));
    }
    public Department findDepartmentByIdForAdmin(String subBranchId, String adminId){

        Userr found = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Not found!"));

        String mainDepartmentId = found.getDepartmentId();

        if(departmentRepository.existsByIdOfMainBranchAndId(mainDepartmentId, subBranchId)
                            ||
                departmentRepository.existsById(subBranchId)){
            //firstly, it checks if his branch is the main
            return getDepartmentById(subBranchId);
        }
        throw new BadRequestException("You don't have an access to see this department's data!");
    }


    public void acceptRequest(String requestId) {
        RequestForFixedValue requestForFixedValue =
                forChangingValueRepository.findById(requestId)
                        .orElseThrow(() -> new BadRequestException("Request not found!"));
        requestForFixedValue.setStatus(RequestStatus.ACCEPTED);

        forChangingValueRepository.save(requestForFixedValue);

    }

    public void declineRequest(String requestId, String reason) {
        RequestForFixedValue requestForFixedValue =
                forChangingValueRepository.findById(requestId)
                        .orElseThrow(() -> new BadRequestException("Request not found!"));
        requestForFixedValue.setStatus(RequestStatus.DECLINED);
        requestForFixedValue.setReason(reason);

        forChangingValueRepository.save(requestForFixedValue);


    }
}
