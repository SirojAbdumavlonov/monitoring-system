package com.example.monitoringsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;
@Entity
@Table(name = "column_names")
public class ColumnNames {
    @Id
    private Long id;
    private String columnName;
}
