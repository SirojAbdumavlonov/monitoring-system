package com.example.monitoringsystem.payload;

import java.util.List;

public class ColumnNames {
    public ColumnNames(){}
    public ColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    private List<String> columnNames;
}
