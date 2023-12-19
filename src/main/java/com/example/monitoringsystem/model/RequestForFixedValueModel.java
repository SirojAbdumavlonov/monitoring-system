package com.example.monitoringsystem.model;


public record RequestForFixedValueModel(String columnName, Object oldValue, Object newValue,
                                            String requestType, String message, String departmentId) {
}
