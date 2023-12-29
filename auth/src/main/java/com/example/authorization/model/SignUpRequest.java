package com.example.authorization.model;

import lombok.Builder;



@Builder
public record SignUpRequest(String userId, String fullName, String password, String departmentName,
                            String role) {

}
