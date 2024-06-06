package com.example.monitoringsystem.model;

import com.example.monitoringsystem.entity.NewColumnEfficiency;

import java.util.List;

public record NewColumnsEfficiencyAndItsTotal(
        List<NewColumnEfficiency> newColumnEfficiencies,
        double totalOfNewColumnEfficiencies
        ) {
}
