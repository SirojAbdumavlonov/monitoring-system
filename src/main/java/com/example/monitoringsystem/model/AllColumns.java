package com.example.monitoringsystem.model;

import lombok.Builder;


import java.util.List;


@Builder
public record AllColumns(String departmentId, int bankomats,
                         int computers, int keyboard, int printer,int mouse,
                         int monitor, int employees, List<NewColumnModel> newColumns) {

}
