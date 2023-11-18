package com.example.monitoringsystem.model;

import lombok.Data;

@Data

public class SignUpRequest {
    private String id;
    private String fullName;
    private String password;
    private String departmentName;
}
