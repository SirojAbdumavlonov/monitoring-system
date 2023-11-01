package com.example.monitoringsystem.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Efficiency {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ElementCollection
    private List<Double> totalEfficiencies;


}
