package com.example.monitoringsystem.model;

import lombok.Data;

@Data
public class AcceptOrDeclineRequest {
    private String reason;
    private ExactValuesDTO exactValuesDTO;
    private DepartmentDTO departmentDTO;
}
