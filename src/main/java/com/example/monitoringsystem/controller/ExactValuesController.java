package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.model.*;
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
import org.springframework.security.core.userdetails.UserDetails;
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

        return ResponseEntity.ok(new ApiResponse("Saved successfully!"));
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
    public ResponseEntity<?> getDailyData(@CurrentUserId UserDetails user,
                                          @RequestParam(required = false) LocalDate date,
                                          @RequestParam(name = "chosen-department",required = false) String chosenDepartment,
                                          @RequestParam(name = "from", required = false) LocalDate from,
                                          @RequestParam(name = "to", required = false) LocalDate to,
                                          @RequestParam(name = "time-range", required = false, defaultValue = "thisWeek") String timeRange,
                                          @RequestParam(name = "month", required = false) String monthName,
                                          @RequestParam(name = "last-n-days", required = false) int lastNDays,
                                          @RequestParam(name = "option", required = false) String option){
        //Option can be history only(poka chto)


        //View is needed, if I want to see other departments' data(sub-branches)

        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if(!option.equals("history")) {
            List<ValueWithEfficiency> columnsList =
                    exactColumnsService.getPreviousDaysData
                            (user.getUsername(), date, chosenDepartment, from, to,
                                    timeRange, monthName, lastNDays, authorities);

            return ResponseEntity.ok(columnsList);
        }

        Object history =
                exactColumnsService.getHistoryOfTableFilling
                        (user.getUsername(), date, chosenDepartment, from, to,
                        timeRange, monthName, lastNDays, authorities);

        return ResponseEntity.ok(history);

//        ColumnNames columnNames =
//                exactColumnsService.getNamesOfColumns(userId);

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
//        logger.info("Error in GET request with param: {}", date);
//        throw new BadRequestException("Error");
    }

    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    @PutMapping("/update/daily-data/{departmentId}")
    public ResponseEntity<?> updateFieldInDb(@PathVariable String departmentId,
                                             @CurrentUserId UserDetails user,
                                             @RequestBody UpdateRequest updateRequest){
        String currentUserId = user.getUsername();

        exactValuesService.updateColumns(updateRequest, departmentId, currentUserId);

        logger.info("Data is successfully updated!");

        return ResponseEntity.ok(new ApiResponse("Changed successfully!"));
    }

    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    @PostMapping("/update/fixed-data")
    public ResponseEntity<?> changeFixedValue(
            @CurrentUserId UserDetails user,
            @RequestBody RequestForFixedValueModel requestForFixedValue){

        //request type can be values which are in RequestType class
        String userId = user.getUsername();

        exactValuesService.requestForChangingFixedValue(requestForFixedValue, userId);

        return ResponseEntity.ok(new ApiResponse("Request sent successfully!"));
    }


}
