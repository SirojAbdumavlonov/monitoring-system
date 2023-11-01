package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.model.AllColumns;
import com.example.monitoringsystem.security.CurrentUserId;
import com.example.monitoringsystem.service.ExactValuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class ExactValuesController {

    private final ExactValuesService exactValuesService;


    // fixed data
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping("/save/first-data")
    public ResponseEntity<?> saveSettingsData(@RequestBody AllColumns allColumns) {
        exactValuesService.saveData(allColumns);
        return ResponseEntity.ok("Saved successfully!");
    }


    // daily configuration of the table which is controlled by admins

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping("/save/daily-data")
    public ResponseEntity<?> saveDailyData(@RequestBody AllColumns allColumns) {
        exactValuesService.saveDailyData(allColumns);
        return ResponseEntity.ok("Saved successfully!");
    }








}
