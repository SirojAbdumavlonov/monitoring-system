package com.example.department.controller;

import com.util.model.UpdatableDepartment;
import com.example.department.entity.Department;
import com.example.department.service.DepartmentService;
import com.util.model.DepartmentAndSubBranches;
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
    @GetMapping("/dept-of-user/{userId}")
    public DepartmentAndSubBranches getDepartmentIdOfUserAndSubBranches(@PathVariable String userId){
        return departmentService.getDepartmentAndSubBranches(userId);
    }
    @GetMapping("/check-dept-subbranches/{departmentId}-{chosenDepartmentId}")
    public Boolean checkDeptAndSubBranches(@PathVariable String departmentId,
                                           @PathVariable String chosenDepartmentId){
        return departmentService.checkExistenceDepartmentId
                (departmentId, chosenDepartmentId);
    }
    @GetMapping("/check-dept-access/{subBranchDepartmentId}-{adminId}")
    public Boolean checkAccessibilityOfUserToDepartment(@PathVariable String subBranchDepartmentId,
                                                        @PathVariable String adminId){
        departmentService.findDepartmentByIdForAdmin(subBranchDepartmentId, adminId);
        return true;
    }
}
