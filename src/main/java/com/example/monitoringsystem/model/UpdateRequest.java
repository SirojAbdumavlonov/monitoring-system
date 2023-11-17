package com.example.monitoringsystem.model;

import lombok.Data;

@Data
public class UpdateRequest {
    private ExactColumnsDTO exactColumns;
    private int oldValue;
    private int newValue;
    private String columnName;
//    LocalDate date;
}
