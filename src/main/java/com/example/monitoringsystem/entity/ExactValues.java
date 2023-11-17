package com.example.monitoringsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;



@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExactValues {//Fixed values
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private int bankomats;
    private int computers;
    private int keyboard;
    private int printer;
    private int mouse;
    private int monitor;
    private int employees;

    @ManyToOne
    @JoinColumn(
            name = "department_id"
    )
    private Department department;
}
