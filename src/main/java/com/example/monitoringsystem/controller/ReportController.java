package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.security.CurrentUserId;
import com.example.monitoringsystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/report")
    public ResponseEntity<?> getReports(@CurrentUserId String userId,
                                        @RequestParam(name = "from_date", required = false) LocalDate from,
                                        @RequestParam(name = "to_date", required = false) LocalDate to,
                                        @RequestParam(name = "department", required = false) String departmentName){




        return null;
    }
}
