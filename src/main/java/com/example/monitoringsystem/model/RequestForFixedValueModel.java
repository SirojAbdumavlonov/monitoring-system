package com.example.monitoringsystem.model;

import lombok.Data;

@Data
public class RequestForFixedValueModel {
    private String adminId;//Who requested
    private String columnName;
    private Object oldValue;
    private Object newValue;
    private String requestType;
    private String message;
    private String departmentId;
}
