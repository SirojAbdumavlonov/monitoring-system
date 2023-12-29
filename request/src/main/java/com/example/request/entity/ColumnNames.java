package com.example.request.entity;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "columnNames")
@Getter
public class ColumnNames {
    private String columnName;

}
