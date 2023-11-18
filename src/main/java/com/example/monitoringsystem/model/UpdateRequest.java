package com.example.monitoringsystem.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateRequest {
    private ExactColumnsDTO exactColumns;
    private int oldValue;
    private int newValue;
    private String columnName;
    private LocalDate date;
}
