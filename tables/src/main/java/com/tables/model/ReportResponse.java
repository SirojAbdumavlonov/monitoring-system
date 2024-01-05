package com.tables.model;

import com.tables.entity.ExactValues;
import com.tables.entity.TableChanges;


import java.util.List;


public record ReportResponse(   List<ValueWithEfficiency> valueWithEfficiencyList,
        ExactValues fixedValues,
        List<TableChanges> tableChanges) {


}
