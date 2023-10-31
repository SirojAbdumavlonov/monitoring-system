package com.example.monitoringsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExactColumns {
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
    @OneToMany
    @JoinColumn(
            name = "new_column"
    )
    private List<NewColumn> newColumns;


}
