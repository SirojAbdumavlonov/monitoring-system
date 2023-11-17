package com.example.monitoringsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    private String departmentName;
    @Id
    private String id;
    private String address;

    @OneToOne
    @JoinColumn(
            name = "location_id"
    )
    private Location location;


    private String idOfMainBranch;


}
