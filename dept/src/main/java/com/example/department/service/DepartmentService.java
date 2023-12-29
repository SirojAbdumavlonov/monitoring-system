package com.example.department.service;

import com.example.authorization.entity.Userr;
import com.example.department.entity.*;
import com.example.authorization.exception.BadRequestException;
import com.example.department.model.*;
import com.example.department.repository.*;
import com.example.department.tool.MapperProperties;
import com.util.constants.Localhost;
import com.util.model.DepartmentDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final WebClient webClient;
    private final MapperProperties mapperProperties;

    public void saveAndUpdate(String departmentId, DepartmentDTO departmentDTO){

        Department department = departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new com.util.exception.BadRequestException("Wrong department id!"));

        mapperProperties.copyNonNullProperties(departmentDTO, department);

        departmentRepository.save(department);
    }

    public Department getByDepartmentId(String departmentId){
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BadRequestException("Wrong department id!"));
    }

    public Boolean existsById(String departmentId){
        return departmentRepository.existsById(departmentId);
    }

    public Department getDepartmentByName(String departmentName){
        return departmentRepository.findByDepartmentName
                (departmentName).orElseThrow(
                        () -> new BadRequestException("Not found!"));
    }
    @Transactional
    public void saveNewDepartment(NewDepartment newDepartment) {
        if(newDepartment.idOfMainBranch() != null) {
            if (!departmentRepository.existsById(newDepartment.idOfMainBranch())) {
                throw new BadRequestException("There is no department with " + newDepartment.idOfMainBranch() + " id");
            }
        }

        Location location = Location.builder()
                .lon(newDepartment.lon())
                .lat(newDepartment.lat())
                .build();

        Department department = Department.builder()
                .address(newDepartment.address())
                .departmentName(newDepartment.departmentName())
                .location(location)
                .idOfMainBranch(newDepartment.idOfMainBranch())
                .id(newDepartment.id())
                .build();

        departmentRepository.save(department);
    }
    private Department getDepartmentById(String departmentId){
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BadRequestException("Department not found"));
    }

    public Department findDepartmentOfUser(String userId) {

        Userr found = webClient.get()
                .uri(Localhost.AUTH + "user/" + userId)
                .retrieve()
                .bodyToMono(Userr.class)
                .block();

        return getDepartmentById(found.getDepartmentId());
    }

    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }
    public List<Department> findDepartmentWithItsSubBranches(String userId){


        String departmentId = findDepartmentOfUser(userId).getId();

        Department department = getDepartmentById(departmentId);

        List<Department> getAllDepartments = new java.util.ArrayList<>(List.of(department));

        getAllDepartments.addAll(departmentRepository.findByIdOfMainBranch(departmentId));

        return getAllDepartments;
    }
    public Department findDepartmentByItsIdForSuperAdmin(String departmentId){//this method is for super admin
        return departmentRepository.findById(departmentId).
                orElseThrow(() -> new BadRequestException("There is no department with this id"));
    }
    public Department findDepartmentByIdForAdmin(String subBranchId, String adminId){

        String mainDepartmentId = findDepartmentOfUser(adminId).getId();

        if(departmentRepository.existsByIdOfMainBranchAndId(mainDepartmentId, subBranchId)
                            ||
                departmentRepository.existsById(subBranchId)){
            //firstly, it checks if his branch is the main
            return getDepartmentById(subBranchId);
        }
        throw new BadRequestException("You don't have an access to see this department's data!");
    }

}
