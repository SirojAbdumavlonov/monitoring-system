package com.example.monitoringsystem.entity;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "columnNames")
@Getter
public class ColumnNames {
    private String columnName;
    public ColumnNames(){}
    public ColumnNames(String columnName){
        this.columnName = columnName;
    }

}
