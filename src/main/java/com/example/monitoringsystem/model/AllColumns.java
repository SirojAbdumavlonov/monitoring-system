package com.example.monitoringsystem.model;

import com.example.monitoringsystem.entity.NewColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllColumns {

    private Long departmentId;

    private int bankomats;
    private int computers;
    private int keyboard;
    private int printer;
    private int mouse;
    private int monitor;
    private int employees;

    private List<NewColumnModel> newColumns;
}
