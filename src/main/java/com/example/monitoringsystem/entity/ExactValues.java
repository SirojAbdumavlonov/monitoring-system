package com.example.monitoringsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;


@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExactValues {
    //Fixed values
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int bankomats;
    private int computers;
    private int keyboard;
    private int printer;
    private int mouse;
    private int monitor;
    private int employees;
    private LocalDate localDate;

    private String departmentId;

    private List<NewColumnsToExactValue> newColumnsToExactValueList;
}
