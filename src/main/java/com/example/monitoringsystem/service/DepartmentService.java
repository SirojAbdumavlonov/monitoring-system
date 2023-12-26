package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
import com.example.monitoringsystem.constants.RequestType;
import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.*;
import com.example.monitoringsystem.repository.*;
import com.example.monitoringsystem.tool.MapperProperties;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RequestForChangingValueRepository forChangingValueRepository;
    private final TableChangesRepository changesRepository;
    private final ExactValuesRepository exactValuesRepository;
    private final ColumnNamesRepository columnNamesRepository;
    private final MapperProperties mapperProperties;

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
        System.out.println("userId = " + userId);
        Userr found = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Not found!"));

        return getDepartmentById(found.getDepartmentId());
    }

    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }
    public List<Department> findDepartmentWithItsSubBranches(String userId){
        Userr found = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Not found!"));

        String departmentId = found.getDepartmentId();

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

        Userr found = userRepository.findByUserId(adminId)
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

    @Transactional
    public ResponseEntity<?> acceptRequest(String requestId, AcceptOrDeclineRequest request) {

        RequestForFixedValue requestForFixedValue =
                forChangingValueRepository.findById(requestId)
                        .orElseThrow(() -> new BadRequestException("Request not found!"));

        requestForFixedValue.setStatus(RequestStatus.ACCEPTED);


        if (requestForFixedValue.getRequestType().equals(RequestType.FIXED_VALUE)){

            ExactValues exactValues = exactValuesRepository.
                    findByDepartmentId(requestForFixedValue.getDepartmentId());

            ExactValuesDTO exactValuesDTO = request.exactValuesDTO();
            log.info("exact = {}", exactValues);
            log.info("dto = {}", exactValuesDTO);

            for (ChangedColumnWithMessage<Object> changedColumnWIthMessage : requestForFixedValue.getChangedColumnWithMessages()) {

                if (columnNamesRepository.existsByColumnName(changedColumnWIthMessage.columnName())) {

                    mapperProperties.copyNonNullProperties(exactValuesDTO, exactValues);

                    log.info("exact values = {}", exactValues);
                } else {
                    if (exactValues.getNewColumnsToExactValueList() != null) {
                        for (NewColumnsToExactValue newColumn : exactValues.getNewColumnsToExactValueList()) {
                            if (newColumn.getName().equals(changedColumnWIthMessage.columnName())) {

                                newColumn.setValue((Integer) changedColumnWIthMessage.newValue());
                            }
                        }
                    }
                }
            }
            exactValuesRepository.save(exactValues);
        }
        else if(requestForFixedValue.getRequestType().equals(RequestType.DEPARTMENT_VALUE)){
            Department department = departmentRepository
                    .findById(requestForFixedValue.getDepartmentId())
                    .orElseThrow(() -> new BadRequestException("Wrong department id!"));

            DepartmentDTO departmentDTO =
                    request.departmentDTO();

            mapperProperties.copyNonNullProperties(departmentDTO, department);

            departmentRepository.save(department);
        }
        log.info("Changes started");
        log.info("{}", requestForFixedValue.getChangedColumnWithMessages());
        for (ChangedColumnWithMessage changedColumnWIthMessage : requestForFixedValue.getChangedColumnWithMessages()) {
            TableChanges tableChanges =
                    TableChanges.builder()
                            .type(requestForFixedValue.getRequestType())
                            .columnName(changedColumnWIthMessage.columnName())
                            .newValue(changedColumnWIthMessage.newValue())
                            .oldValue(changedColumnWIthMessage.oldValue())
                            .userId(requestForFixedValue.getAdminId())
                            .build();
            log.info("Table changes {}", tableChanges);
            changesRepository.save(tableChanges);
        }

        forChangingValueRepository.save(requestForFixedValue);

        return ResponseEntity.ok(new ApiResponse("Accepted successfully!"));
    }
    @Transactional
    public ResponseEntity<?> declineRequest(String requestId, String reason) {
        RequestForFixedValue requestForFixedValue =
                forChangingValueRepository.findById(requestId)
                        .orElseThrow(() -> new BadRequestException("Request not found!"));

        requestForFixedValue.setStatus(RequestStatus.DECLINED);
        //todo: give a reason if request is declined
        requestForFixedValue.setReason(reason);

        forChangingValueRepository.save(requestForFixedValue);
        return ResponseEntity.ok(new ApiResponse("Request was declined!"));
    }
}
