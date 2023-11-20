package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.constants.Tab;
import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.AllColumns;
import com.example.monitoringsystem.model.RequestForFixedValueModel;
import com.example.monitoringsystem.model.UpdateRequest;
import com.example.monitoringsystem.model.WholeDepartment;
import com.example.monitoringsystem.payload.ColumnNames;
import com.example.monitoringsystem.payload.DailyDataReturn;
import com.example.monitoringsystem.security.CurrentUserId;
import com.example.monitoringsystem.service.ExactColumnsService;
import com.example.monitoringsystem.service.ExactValuesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExactValuesController {

    private final ExactValuesService exactValuesService;

    private final ExactColumnsService exactColumnsService;
    private final Logger logger = LoggerFactory.getLogger(ExactValuesController.class);

    // fixed data
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER')")
    @PostMapping("/save/fixed-data")
    public ResponseEntity<?> saveSettingsData(@RequestBody AllColumns allColumns) {

        exactValuesService.saveData(allColumns);

        logger.info("No mistake in saving fixed data!");

        return ResponseEntity.ok("Saved successfully!");
    }

    // daily configuration of the table which is controlled by admins
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER')")
    @PostMapping("/save/daily-data")
    public ResponseEntity<?> saveDailyData(@RequestBody AllColumns allColumns) {

        Object wholeDepartment = exactValuesService.saveDailyData(allColumns);

        logger.info("No mistake in saving daily data!");

        return ResponseEntity.ok(wholeDepartment);
    }
    @GetMapping("/daily-data")
    public ResponseEntity<?> getDailyData(@CurrentUserId String userId,
                                          @RequestParam(required = false) String date,
                                          @RequestParam(required = false) String view,
                                          @RequestParam(name = "from", required = false) LocalDate from,
                                          @RequestParam(name = "to", required = false) LocalDate to,
                                          @RequestParam(name = "time-range", required = false, defaultValue = "thisWeek") String timeRange,
                                          @RequestParam(name = "month", required = false) String monthName){

        //View is needed, if I want to see other departments' data(sub-branches)

        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            Object columnsList =
                    exactColumnsService.getPreviousDaysData(userId, date, view, from, to, timeRange, monthName);
        }
        else if(authorities.stream().anyMatch(a -> a.getAuthority().equals("SUPER_ADMIN"))){

        }
        else{//it's for admin

        }
        ColumnNames columnNames =
                exactColumnsService.getNamesOfColumns(userId);

//        if(tab.equals(Tab.ALL)){
//            List<ExactColumns> columnsList =
//                    exactColumnsService.getPreviousDaysData(userId);
//            return ResponseEntity.ok(new DailyDataReturn
//                    <>(columnNames, columnsList));
//        }
//        else if(tab.equals(Tab.TODAY)){
//            ExactColumns columns =
//                    exactColumnsService.getTodayDailyFilledData(today, userId);
//
//            return ResponseEntity.ok(new DailyDataReturn<>(columnNames, columns));
//        }
        logger.info("Error in GET request with param: {}", tab);
        throw new BadRequestException("Error");
    }

    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    @PutMapping("/daily-changes/update/{departmentId}")
    public ResponseEntity<?> updateFieldInDb(@PathVariable String departmentId,
                                             @CurrentUserId String currentUserId,
                                             @RequestBody UpdateRequest updateRequest){
        exactValuesService.updateColumns(updateRequest, departmentId, currentUserId);

        logger.info("Data is successfully updated!");

        return ResponseEntity.ok("Changed successfully!");
    }

    @PostMapping("/update/fixed-data")
    public ResponseEntity<?> changeFixedValue(
            @CurrentUserId String userId,
            @RequestBody RequestForFixedValueModel requestForFixedValue){

        exactValuesService.requestForChangingFixedValue(requestForFixedValue, userId);

        logger.info("Request sent!");

        return ResponseEntity.ok("Request sent successfully!");
    }

}
