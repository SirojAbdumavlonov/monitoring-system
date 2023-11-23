package com.example.monitoringsystem.model;

import com.example.monitoringsystem.entity.Efficiency;
import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.entity.ExactValues;
import com.example.monitoringsystem.entity.TableChanges;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    List<ValueWithEfficiency> valueWithEfficiencyList;
    ExactValues fixedValues;
    List<TableChanges> tableChanges;

}
