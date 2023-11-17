package com.example.monitoringsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String columnId;

    private String name;

    @ElementCollection
    @CollectionTable(name = "element_values",joinColumns = @JoinColumn(name = "new_column_id"))
    private List<ValueWithDate> values;

    @ManyToOne
    @JoinColumn(
            name = "department_id"
    )
    private Department department;


}
