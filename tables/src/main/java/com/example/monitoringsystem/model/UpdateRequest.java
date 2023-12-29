package com.example.monitoringsystem.model;


import java.time.LocalDate;
import java.util.List;


public record UpdateRequest(ExactColumnsDTO exactColumns,
                            List<ChangedColumn> changedColumns, LocalDate date) {
}
