package com.example.department.model;

import com.example.department.entity.Location;

public record DepartmentDto(String address, String departmentName,
                            Location location, String idOfMainBranch) {

}