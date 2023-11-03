package com.example.monitoringsystem.payload;

public class DailyDataReturn<T, I> {
    public DailyDataReturn(){}
    private T columns;
    private I values;

    public DailyDataReturn(T columns, I values) {
        this.columns = columns;
        this.values = values;
    }

    public T getColumns() {
        return columns;
    }

    public void setColumns(T columns) {
        this.columns = columns;
    }

    public I getValues() {
        return values;
    }

    public void setValues(I values) {
        this.values = values;
    }
}
