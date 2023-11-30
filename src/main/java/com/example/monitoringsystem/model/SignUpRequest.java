package com.example.monitoringsystem.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequest {
    private String id;
    private String fullName;
    private String password;
    private String departmentName;
}
