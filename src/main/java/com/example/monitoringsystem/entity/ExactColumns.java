package com.example.monitoringsystem.entity;

import com.example.monitoringsystem.entity.audit.DateAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExactColumns extends DateAudit {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private int bankomats;
    private int computers;
    private int keyboard;
    private int printer;
    private int mouse;
    private int monitor;
    private int employees;


    @ManyToOne
    @JoinColumn(
            name = "efficiency"
    )
    private Efficiency efficiency;

    @ManyToOne
    @JoinColumn(
            name = "department_id"
    )
    private Department department;
//    @ElementCollection
//    private List<Long> newColumnsId;
}
