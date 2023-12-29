package com.util.model;

import com.example.monitoringsystem.entity.Location;


public record DepartmentDTO(String address, String departmentName,
                            Location location, String idOfMainBranch) {

}
