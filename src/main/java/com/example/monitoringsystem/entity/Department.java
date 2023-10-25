package com.example.monitoringsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Department {
    private String departmentName;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String address;

    @OneToOne
    @JoinColumn(
            name = "location_id"
    )
    private Location location;

}
