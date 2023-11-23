package com.example.monitoringsystem.model;

import com.example.monitoringsystem.entity.Location;
import lombok.Data;

@Data
public class DepartmentDTO {
    private String id;
    private String address;

    private String departmentName;

    private Location location;

    private String idOfMainBranch;
}
