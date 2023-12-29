package com.util.model;


import com.util.model.DepartmentDTO;

public record AcceptOrDeclineRequest(String reason,
                                     ExactValuesDTO exactValuesDTO,
                                     DepartmentDTO departmentDTO){

}
