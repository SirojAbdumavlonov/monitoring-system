package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.model.RequestForFixedValueModel;
import com.example.monitoringsystem.security.CurrentUserId;
import com.example.monitoringsystem.service.DepartmentService;
import com.example.monitoringsystem.service.ExactValuesService;
import com.example.monitoringsystem.service.RequestsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.monitoringsystem.model.NewDepartment;


import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final ExactValuesService exactValuesService;
    private final RequestsService requestsService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @GetMapping("/")
    public ResponseEntity<?> getAllDepartments(){

        List<Department> allDepartments = departmentService.findAllDepartments();

        return ResponseEntity.ok(allDepartments);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/add-department")
    public ResponseEntity<?> addingDepartment(@Valid @RequestBody NewDepartment newDepartment){

        departmentService.saveNewDepartment(newDepartment);

        return ResponseEntity.ok("Saved successfully!");
    }

    @GetMapping("/add-info")
    public ResponseEntity<?> getAllData(@CurrentUserId String userId,
                                        @RequestParam(name = "view", required = false, defaultValue = "all") String viewOption) {

        //View option is used, if super admin wants to see all departments' data,
        //or just one, which he/she has chosen
        //View can be:
        //ALL: to see all departments' data
        //BRANCH_ID: to see only one department's data
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            return ResponseEntity.ok(departmentService.findDepartmentOfUser(userId));
        }
        else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            if(viewOption.equals("all")) {
                return ResponseEntity.ok(departmentService.findDepartmentWithItsSubBranches(userId));
            }
            return ResponseEntity.ok(departmentService.findDepartmentByIdForAdmin(viewOption, userId));
            //if this admin is working in the main branch of other sub-branches
            //It will show data of chosen by him/her department's data
        }
        else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("SUPER_ADMIN"))) {
            if(viewOption.equals("all")) {
                return ResponseEntity.ok(departmentService.findAllDepartments());
            }
            return ResponseEntity.ok(departmentService.findDepartmentByItsIdForSuperAdmin(viewOption));
            //find only one department with chosen one
        }
        return ResponseEntity.ok("Not found!");
    }

    @PostMapping("/update/add-info")
    public ResponseEntity<?> changeDepartmentValue(
            @CurrentUserId String userId,
            @RequestBody RequestForFixedValueModel requestForFixedValue){

        exactValuesService.requestForChangingFixedValue(requestForFixedValue, userId);

        return ResponseEntity.ok("Request sent successfully!");
    }

    @PutMapping("/request/{requestId}")
    public ResponseEntity<?> acceptOrDeclineRequest(@PathVariable String requestId,
                                                    @RequestParam(name = "opt") String option,
                                                    @RequestBody String reason){
        //Option is used for accepting or declining request
        //Option can be only accept or decline
        if(option.equals("accept")){
            departmentService.acceptRequest(requestId);
        }
        else if(option.equals("decline")){
            departmentService.declineRequest(requestId, reason);
        }
        return null;
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @GetMapping("/request")
    public ResponseEntity<?> getAllRequests(@RequestParam(name = "opt", defaultValue = "waiting") String opt){
        //show requests which are in waiting status
        if(opt.equals("all")){
            return ResponseEntity.ok(requestsService.getAllValues());
        }

        return ResponseEntity.ok(requestsService.getValues(opt.toLowerCase()));
    }
}
