package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.security.CurrentUserId;
import com.example.monitoringsystem.service.ExactColumnsService;
import com.example.monitoringsystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ExactColumnsService exactColumnsService;

    @GetMapping("/report")
    public ResponseEntity<?> getReports(@CurrentUserId String userId,
                                        @RequestParam(required = false) LocalDate date,
                                        @RequestParam(name = "chosen-department",required = false) String chosenDepartment,
                                        @RequestParam(name = "from", required = false) LocalDate from,
                                        @RequestParam(name = "to", required = false) LocalDate to,
                                        @RequestParam(name = "time-range", required = false, defaultValue = "thisWeek") String timeRange,
                                        @RequestParam(name = "month", required = false) String monthName,
                                        @RequestParam(name = "last-n-days", required = false) int lastNDays,
                                        @RequestParam(name = "option", required = false) String option){

        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Object columnsList =
                reportService.getReportData
                        (userId, date, chosenDepartment, from, to,
                                timeRange, monthName, lastNDays, authorities, option);



        return null;
    }
}