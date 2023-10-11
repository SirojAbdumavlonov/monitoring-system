package com.example.monitoringsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue( // serial DB
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String fullName;
    private RoleName roleName;

    @ManyToOne
    @JoinColumn(
            name = "department_id"
    )
    private Department department;
}
