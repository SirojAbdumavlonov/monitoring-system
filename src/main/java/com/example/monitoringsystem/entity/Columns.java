package com.example.monitoringsystem.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Columns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "column_id")
    private Long columnId;

    private String name;

    private String value;

    @ManyToOne
    @JoinColumn(
            name = "department_id"
    )
    private Department id;
}
