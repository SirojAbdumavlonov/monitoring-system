package com.example.monitoringsystem.model;

import com.example.monitoringsystem.entity.Efficiency;
import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.entity.ExactValues;
import lombok.Data;

import java.util.List;

@Data
public class ReportResponse {
    private List<String> columnNames;

    private List<ExactColumns> dailyValues;
    private List<ExactValues> exactValues;
    private List<Efficiency> efficiencies;
}
