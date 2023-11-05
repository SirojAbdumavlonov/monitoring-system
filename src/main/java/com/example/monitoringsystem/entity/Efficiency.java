package com.example.monitoringsystem.entity;

import com.example.monitoringsystem.entity.audit.DateAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Efficiency extends DateAudit {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private Double bankomats;
    private Double computers;
    private Double keyboard;
    private Double printer;
    private Double mouse;
    private Double monitor;
    private Double employees;
    private Double totalEfficiency;
    @ManyToOne
    @JoinColumn(
            name = "department_id"
    )
    private Department department;


}
