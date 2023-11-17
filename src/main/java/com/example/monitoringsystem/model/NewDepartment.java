package com.example.monitoringsystem.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class NewDepartment {
    private String id;

    private String departmentName;

    private String address;
    private double lon;
    private double lat;
    private String idOfMainBranch;
}
