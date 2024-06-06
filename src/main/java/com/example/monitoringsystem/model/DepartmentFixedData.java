package com.example.monitoringsystem.model;

import com.example.monitoringsystem.entity.ExactValues;

import java.util.HashMap;
import java.util.List;

public record DepartmentFixedData(String departmentId, String id, HashMap<Object, Object> data) {

}
