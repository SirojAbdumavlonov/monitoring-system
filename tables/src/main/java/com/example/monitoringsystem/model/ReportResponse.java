package com.example.monitoringsystem.model;

import com.example.monitoringsystem.entity.ExactValues;
import com.example.monitoringsystem.entity.TableChanges;


import java.util.List;


public record ReportResponse(   List<ValueWithEfficiency> valueWithEfficiencyList,
        ExactValues fixedValues,
        List<TableChanges> tableChanges) {


}
