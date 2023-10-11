package com.example.monitoringsystem.entity;

import jakarta.persistence.*;

@Entity
public class Department {
    private String departmentName;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private String id;
    private String address;

    @OneToOne
    @JoinColumn(
            name = "location_id"
    )
    private Location location;

}
