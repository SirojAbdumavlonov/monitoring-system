package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.model.AllColumns;
import com.example.monitoringsystem.model.ApiResponse;
import com.example.monitoringsystem.model.UpdateRequest;
import com.example.monitoringsystem.model.ValueWithEfficiency;
import com.example.monitoringsystem.security.CurrentUserId;
import com.example.monitoringsystem.service.ExactColumnsService;
import com.example.monitoringsystem.service.ExactValuesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class ExactColumnsController {
    private final ExactColumnsService exactColumnsService;

    @GetMapping("/daily-data")
    public ResponseEntity<?> getDailyData(@CurrentUserId UserDetails user,
                                          @RequestParam(required = false) LocalDate date,
                                          @RequestParam(name = "chosen-department",required = false) String chosenDepartment,
                                          @RequestParam(name = "from", required = false) LocalDate from,
                                          @RequestParam(name = "to", required = false) LocalDate to,
                                          @RequestParam(name = "time-range", required = false, defaultValue = "thisWeek") String timeRange,
                                          @RequestParam(name = "month", required = false) String monthName,
                                          @RequestParam(name = "last-n-days", required = false) Integer lastNDays,
                                          @RequestParam(name = "option", required = false, defaultValue = "opt") String option){
        //Option can be history only(poka chto)


        //View is needed, if I want to see other departments' data(sub-branches)

        System.out.println("User details: " + user.getUsername());
        System.out.println(user);

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
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER')")
    @PostMapping("/save/daily-data/{departmentId}")
    public ResponseEntity<?> saveDailyData(@RequestBody AllColumns allColumns,
                                           @PathVariable String departmentId) {

        Object wholeDepartment = exactColumnsService.saveDailyData(allColumns, departmentId);

        log.info("No mistake in saving daily data!");

        return ResponseEntity.ok(wholeDepartment);
    }

    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    @PutMapping("/update/daily-data/{departmentId}")
    public ResponseEntity<?> updateFieldInDb(@PathVariable String departmentId,
                                             @CurrentUserId UserDetails user,
                                             @RequestBody UpdateRequest updateRequest){
        String currentUserId = user.getUsername();

        exactColumnsService.updateColumns(updateRequest, departmentId, currentUserId);

        log.info("Data is successfully updated!");

        return ResponseEntity.ok(new ApiResponse("Changed successfully!"));
    }

}
