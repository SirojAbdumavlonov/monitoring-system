package com.example.monitoringsystem.model;



public record AcceptOrDeclineRequest(String reason,
                                     ExactValuesDTO exactValuesDTO,
                                     DepartmentDTO departmentDTO){

}
