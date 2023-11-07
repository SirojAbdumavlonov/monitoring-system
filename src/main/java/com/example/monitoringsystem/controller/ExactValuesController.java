package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.constants.Tab;
import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.AllColumns;
import com.example.monitoringsystem.model.UpdateRequest;
import com.example.monitoringsystem.payload.ColumnNames;
import com.example.monitoringsystem.payload.DailyDataReturn;
import com.example.monitoringsystem.security.CurrentUserId;
import com.example.monitoringsystem.service.DepartmentService;
import com.example.monitoringsystem.service.ExactColumnsService;
import com.example.monitoringsystem.service.ExactValuesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExactValuesController {

    private final ExactValuesService exactValuesService;
    private final DepartmentService departmentService;
    private final ExactColumnsService exactColumnsService;
    private final Logger logger = LoggerFactory.getLogger(ExactValuesController.class);

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

    @GetMapping("/daily-changes")
    public ResponseEntity<?> getDailyData(@CurrentUserId String userId,
                                          @RequestParam(defaultValue = "today", required = false) String tab){
        LocalDate today = LocalDate.now();
        Long id = Long.valueOf(userId);
        ColumnNames columnNames =
                exactColumnsService.getNamesOfColumns(id);
        if(tab.equals(Tab.ALL)){
            List<ExactColumns> columnsList =
                    exactColumnsService.getPreviousDaysData(id);
            return ResponseEntity.ok(new DailyDataReturn
                    <ColumnNames, List<ExactColumns>>(columnNames, columnsList));
        }
        else if(tab.equals(Tab.TODAY)){
            ExactColumns columns =
                    exactColumnsService.getTodayDailyFilledData(today, id);

            return ResponseEntity.ok(new DailyDataReturn<ColumnNames, ExactColumns>(columnNames, columns));
        }
        logger.info("Error in GET request with param: {}", tab);
        throw new BadRequestException("Error");
    }
    @PutMapping("/daily-changes/update/{departmentId}")
    public ResponseEntity<?> updateFieldInDb(@PathVariable Long departmentId,
                                             @CurrentUserId String currentUserId,
                                             @RequestBody UpdateRequest updateRequest){
        exactValuesService.updateColumns(updateRequest, departmentId, currentUserId);

        return null;
    }
}
