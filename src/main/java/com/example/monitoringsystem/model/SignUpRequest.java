package com.example.monitoringsystem.model;

import lombok.Builder;



@Builder
public record SignUpRequest(String id, String fullName, String password, String departmentName) {
}
