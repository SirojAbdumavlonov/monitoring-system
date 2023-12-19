package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
import com.example.monitoringsystem.constants.RequestType;
import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.AcceptOrDeclineRequest;
import com.example.monitoringsystem.model.DepartmentDTO;
import com.example.monitoringsystem.model.ExactValuesDTO;
import com.example.monitoringsystem.model.NewDepartment;
import com.example.monitoringsystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RequestForChangingValueRepository forChangingValueRepository;
    private final TableChangesRepository changesRepository;
    private final ExactValuesRepository exactValuesRepository;
    private final ColumnNamesRepository columnNamesRepository;
    private final ColumnsMapper columnsMapper;

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
        Userr found = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Not found!"));

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
                orElseThrow(() -> new BadRequestException("There is no department with this id"));
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

    @Transactional
    public void acceptRequest(String requestId, AcceptOrDeclineRequest request) {

        RequestForFixedValue requestForFixedValue =
                forChangingValueRepository.findById(requestId)
                        .orElseThrow(() -> new BadRequestException("Request not found!"));

        requestForFixedValue.setStatus(RequestStatus.ACCEPTED);


        if (requestForFixedValue.getRequestType().equals(RequestType.FIXED_VALUE)){

            ExactValues exactValues = exactValuesRepository.
                    findByDepartmentId(requestForFixedValue.getDepartmentId());

            ExactValuesDTO exactValuesDTO = request.exactValuesDTO();

            if(columnNamesRepository.existsByColumnName(requestForFixedValue.getColumnName())){
                columnsMapper.updateChangedFixedColumn(exactValuesDTO, exactValues);
            }
            else{
                if(!exactValues.getNewColumnsToExactValueList().isEmpty()) {
                    for (NewColumnsToExactValue newColumn : exactValues.getNewColumnsToExactValueList()) {
                        if (newColumn.getName().equals(requestForFixedValue.getColumnName())) {
                            newColumn.setValue((Integer) requestForFixedValue.getNewValue());
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
            columnsMapper.updateChangedDepartmentColumn(departmentDTO, department);
            departmentRepository.save(department);
        }
        TableChanges tableChanges =
                TableChanges.builder()
                        .type(requestForFixedValue.getRequestType())
                        .columnName(requestForFixedValue.getColumnName())
                        .newValue(requestForFixedValue.getNewValue())
                        .oldValue(requestForFixedValue.getOldValue())
                        .userId(requestForFixedValue.getAdminId())
                        .build();

        changesRepository.save(tableChanges);

        forChangingValueRepository.save(requestForFixedValue);

    }
    @Transactional
    public void declineRequest(String requestId, String reason) {
        RequestForFixedValue requestForFixedValue =
                forChangingValueRepository.findById(requestId)
                        .orElseThrow(() -> new BadRequestException("Request not found!"));
        requestForFixedValue.setStatus(RequestStatus.DECLINED);
        requestForFixedValue.setReason(reason);

        forChangingValueRepository.save(requestForFixedValue);

    }
}
