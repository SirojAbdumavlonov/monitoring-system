package com.example.monitoringsystem.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ExactColumns {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ElementCollection
    private List<String> attributesNames;

    @ManyToOne
    @JoinColumn(
            name = "efficiency"
    )
    private Efficiency efficiency;
    @OneToOne
    @JoinColumn(
            name = "new_column"
    )
    private NewColumns newColumns;
}
