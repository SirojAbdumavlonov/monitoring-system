package com.example.monitoringsystem.model;


import java.time.LocalDate;


public record UpdateRequest(     ExactColumnsDTO exactColumns, int oldValue, int newValue,
                                 String columnName, LocalDate date) {
}
