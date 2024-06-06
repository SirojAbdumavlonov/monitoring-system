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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RequestForChangingValueRepository requestRepository;
    private final TableChangesRepository tableChangesRepository;
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


        List<Department> getAllDepartments = new ArrayList<>(List.of(department));

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
    public ResponseEntity<?> acceptRequest(String requestId, AcceptOrDeclineRequest acceptOrDeclineRequest) {

        Request request =
                requestRepository.findById(requestId)
                        .orElseThrow(() -> new BadRequestException("Request not found!"));

        request.setStatus(RequestStatus.ACCEPTED);

        if (request.getRequestType().equals(RequestType.FIXED_VALUE)){

            ExactValues exactValues = exactValuesRepository.
                    findByDepartmentId(request.getDepartmentId());

            ExactValuesDTO exactValuesDTO = acceptOrDeclineRequest.exactValuesDTO();

            getChangedColumnsOfDailyMonitoringPageAndSave(
                    request.getChangedColumnWithMessages(),
                    exactValuesDTO,
                    exactValues
            );
            exactValuesRepository.save(exactValues);
        }
        else if(request.getRequestType().equals(RequestType.DEPARTMENT_VALUE)){

            getChangedColumnsOfDepartmentAndSave(
                    acceptOrDeclineRequest.departmentDTO(), request.getDepartmentId());

        }
        log.info("Table changes are being saved!");

        saveTableChanges(
                request.getChangedColumnWithMessages(),
                request.getRequestType(),
                request.getAdminId()
        );

        requestRepository.save(request);

        return ResponseEntity.ok(new ApiResponse("Accepted successfully!"));
    }

    private void getChangedColumnsOfDepartmentAndSave(
            DepartmentDTO departmentDTO,
            String departmentId
    ){
        Department department = departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new BadRequestException("Wrong department id!"));

        mapperProperties.copyNonNullProperties(departmentDTO, department);

        departmentRepository.save(department);
    }
    private void saveTableChanges(
            List<ChangedColumnWithMessage<Object>> changedColumns,
            String requestType,
            String userId

    ){
        for (ChangedColumnWithMessage changedColumnWIthMessage : changedColumns) {
            TableChanges tableChanges =
                    TableChanges.builder()
                            .type(requestType)
                            .columnName(changedColumnWIthMessage.columnName())
                            .newValue(changedColumnWIthMessage.newValue())
                            .oldValue(changedColumnWIthMessage.oldValue())
                            .userId(userId)
                            .build();
            log.info("Table changes {}", tableChanges);

            tableChangesRepository.save(tableChanges);
        }
    }

    private void getChangedColumnsOfDailyMonitoringPageAndSave(
            List<ChangedColumnWithMessage<Object>> changedColumns,
            ExactValuesDTO exactValuesDTO,
            ExactValues exactValues
    ){
        for (ChangedColumnWithMessage<Object> changedColumn : changedColumns ) {
            if (ifColumnNameExists(changedColumn.columnName())) {
                saveChangesOfExactValues(exactValuesDTO, exactValues);
            }
            else {
                if (ifThereHasNewColumns(exactValues.getExactValueOfNewColumnList())){
                    saveChangesOfNewlyAddedColumns(exactValues, changedColumn);
                }
            }
        }
    }

    private boolean ifColumnNameExists(String columnName){
        return columnNamesRepository.existsByColumnName(columnName);
    }
    private void saveChangesOfExactValues(ExactValuesDTO exactValuesDTO, ExactValues exactValues){ //old columns or default
        mapperProperties.copyNonNullProperties(exactValuesDTO, exactValues);
    }
    private void saveChangesOfNewlyAddedColumns(ExactValues exactValues, ChangedColumnWithMessage<Object> changedColumn){

            for (ExactValueOfNewColumn newColumn : exactValues.getExactValueOfNewColumnList()) {
                if (newColumn.getName().equals(changedColumn.columnName())) {

                    newColumn.setValue((Integer) changedColumn.newValue());
                }
            }
    }
    private boolean ifThereHasNewColumns(List<ExactValueOfNewColumn> values){
        return values == null;
    }

    @Transactional
    public ResponseEntity<?> declineRequest(String requestId, String reason) {
        Request request =
                requestRepository.findById(requestId)
                        .orElseThrow(() -> new BadRequestException("Request not found!"));

        request.setStatus(RequestStatus.DECLINED);
        //todo: give a reason if request is declined
        request.setReason(reason);

        requestRepository.save(request);

        return ResponseEntity.ok(new ApiResponse("Request was declined!"));
    }
}
