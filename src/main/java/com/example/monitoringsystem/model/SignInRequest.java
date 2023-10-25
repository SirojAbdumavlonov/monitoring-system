package com.example.monitoringsystem.model;

import lombok.Data;

@Data
public class SignInRequest {
    private String fullName;
    private String password;
    private String branch;
}
