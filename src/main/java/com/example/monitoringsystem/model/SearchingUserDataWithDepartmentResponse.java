package com.example.monitoringsystem.model;

import java.util.List;

public record SearchingUserDataWithDepartmentResponse(
        List<String> userIds, List<String> fullNames, List<String> departmentNames) {
}
