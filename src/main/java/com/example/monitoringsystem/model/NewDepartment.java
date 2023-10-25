package com.example.monitoringsystem.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class NewDepartment {
    @NonNull
    private String departmentName;
    @NonNull
    private String address;
    private double lon;
    private double lat;
}
