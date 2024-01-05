package com.util.model;

public record AcceptOrDeclineRequest(String reason,
                                     ExactValuesDTO exactValuesDTO,
                                     DepartmentDTO departmentDTO){

}