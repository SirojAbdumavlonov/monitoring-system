package com.request.entity;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@Document(collection = "columnNames")
@Getter
@RedisHash("ColumnNames")
public class ColumnNames implements Serializable {
    private String columnName;
    public ColumnNames(){}
    public ColumnNames(String columnName){
        this.columnName = columnName;
    }
}
