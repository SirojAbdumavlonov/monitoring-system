package com.example.monitoringsystem.model;

import lombok.Data;

@Data
public class RequestForFixedValueModel {
    private String adminId;//Who requested
    private String columnName;
    Object oldValue;
    Object newValue;
    private String requestType;
    private String message;
}
