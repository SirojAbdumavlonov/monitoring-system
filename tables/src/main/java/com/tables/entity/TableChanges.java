package com.tables.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TableChanges{
    private Object oldValue;

    private Object newValue;

    private String columnName;

    private String userId;//user who updated value

    private String type;

    private String departmentId;

    @CreatedDate
    @JsonIgnore
    @Indexed
    private LocalDate createdDate;

    @CreationTimestamp
    @JsonIgnore
    private LocalTime createdTime;
}
