package com.example.monitoringsystem.model;

public record UserDataWithDepartment(
        String userId, String fullName, String role,
        String departmentName, String address) {
}
