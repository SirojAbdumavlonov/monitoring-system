package com.tables.entity;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Efficiency{

    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private String id;

    private Double bankomats;
    private Double computers;
    private Double keyboard;
    private Double printer;
    private Double mouse;
    private Double monitor;
    private Double employees;
    private Double totalEfficiency;
    private LocalDate date;

    private String departmentId;

    private List<NewColumnEfficiency> efficiencyList;

}
