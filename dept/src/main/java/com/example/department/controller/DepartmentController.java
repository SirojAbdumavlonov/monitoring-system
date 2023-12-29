package com.example.department.controller;

import com.example.department.entity.Department;
import com.util.model.ApiResponse;
import com.example.department.model.NewDepartment;
import com.util.security.CurrentUserId;
import com.example.department.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;


    //todo: Method is tested and works
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/")
    public ResponseEntity<?> getAllDepartments(){

        List<Department> allDepartments = departmentService.findAllDepartments();
        //todo: shows all departments only for super_admin
        // it is not working only for super_admin
        return ResponseEntity.ok(allDepartments);
    }
    //todo: Method is tested and works
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/add-department")
    public ResponseEntity<?> addingDepartment(@Valid @RequestBody NewDepartment newDepartment){
        //todo: only super_admin can add new department
        // unauthorized or others users cannot add
        departmentService.saveNewDepartment(newDepartment);

        return ResponseEntity.ok(new ApiResponse("Saved successfully!"));
    }
    //todo: Method is tested and works
    // but not all parameters are checked
    @GetMapping("/add-info")
    public ResponseEntity<?> getAllDataAboutDepartment(@CurrentUserId UserDetails user,
                                        @RequestParam(name = "view", required = false, defaultValue = "all") String viewOption) {
        //todo: show department information
        // for user and admin only their own department
        // super admin can see all
        String userId = user.getUsername();
        log.info("Logged user = {}", user);
        //View option is used, if super admin wants to see all departments' data,
        //or just one, which he/she has chosen
        //View can be:
        //ALL: to see all departments' data
        //BRANCH_ID: to see only one department's data
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        log.info("Authority = {}", authorities);
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            log.info("Return department of USER");
            return ResponseEntity.ok(departmentService.findDepartmentOfUser(userId));
        }
        else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            if(viewOption.equals("all")) {
                log.info("Return departments of ADMIN");
                return ResponseEntity.ok(departmentService.findDepartmentWithItsSubBranches(userId));
            }
            log.info("Return department of ADMIN");
            return ResponseEntity.ok(departmentService.findDepartmentByIdForAdmin(viewOption, userId));
            //if this admin is working in the main branch of other sub-branches
            //It will show data of chosen by him/her department's data
        }
        else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            if(viewOption.equals("all")) {
                log.info("Return departments of SUPER_ADMIN");
                return ResponseEntity.ok(departmentService.findAllDepartments());
            }
            log.info("Return department of SUPER_ADMIN");
            return ResponseEntity.ok(departmentService.findDepartmentByItsIdForSuperAdmin(viewOption));
            //find only one department with chosen one
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Not found!"));
    }


}
