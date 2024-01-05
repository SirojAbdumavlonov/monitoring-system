package com.tables.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;


@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicUpdate
public class ExactValues {
    //Fixed values
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer bankomats;
    private Integer computers;
    private Integer keyboard;
    private Integer printer;
    private Integer mouse;
    private Integer monitor;
    private Integer employees;
    private LocalDate localDate;

    private String departmentId;

    private List<NewColumnsToExactValue> newColumnsToExactValueList;
}
