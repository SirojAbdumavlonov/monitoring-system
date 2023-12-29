package com.example.monitoringsystem.model;

public record ChangedColumn(String columnName,
                            Integer oldValue, Integer newValue) {
}
