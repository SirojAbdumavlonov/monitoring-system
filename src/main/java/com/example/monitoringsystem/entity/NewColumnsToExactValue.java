package com.example.monitoringsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewColumnsToExactValue {
    @Id
    @GeneratedValue(  strategy = GenerationType.IDENTITY  )
    private Long id;

    @jakarta.persistence.Column(name = "column_id")

    private String name;

    private String value;

    @ManyToOne
    @JoinColumn(
            name = "department_id"
    )
    private Department departmentId;


}
