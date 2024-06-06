package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.entity.ExactValues;
import com.example.monitoringsystem.model.*;
import com.example.monitoringsystem.security.CurrentUserId;
import com.example.monitoringsystem.service.ExactValuesService;
import com.example.monitoringsystem.service.RequestsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ExactValuesController {

    private final ExactValuesService exactValuesService;
    private final RequestsService requestsService;

    // fixed data
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER')")
    @PostMapping("/save/fixed-data/{departmentId}")
    public ResponseEntity<?> saveSettingsData(@RequestBody AllColumns allColumns,
                                              @PathVariable String departmentId) {

        exactValuesService.saveData(allColumns, departmentId);

        log.info("No mistake in saving fixed data!");

        return ResponseEntity.ok(new ApiResponse("Saved successfully!"));
    }

    // daily configuration of the table which is controlled by admins
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    @GetMapping("/fixed-data/{departmentId}")
    public ResponseEntity<?> getFixedValue(@PathVariable String departmentId){
        if(departmentId == null || departmentId.isEmpty()){
            return ResponseEntity.ok(new ApiResponse("Please join a department to view their details!"));
        }
        DepartmentFixedData exactValues = exactValuesService.getExactValues(departmentId);
        return ResponseEntity.ok(exactValues);
    }

    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    @PutMapping("/update/fixed-data/{departmentId}")
    public ResponseEntity<?> changeFixedValue(
            @CurrentUserId UserDetails user,
            @PathVariable String departmentId,
            @RequestBody RequestForFixedValueModel<Object> requestForFixedValue){

        //request type can be values which are in RequestType class
        String userId = user.getUsername();

        requestsService.requestForChangingFixedValue(requestForFixedValue, userId, departmentId);

        return ResponseEntity.ok(new ApiResponse("Request sent successfully!"));
    }


}
