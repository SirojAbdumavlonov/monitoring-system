package com.example.monitoringsystem.entity;

import com.example.monitoringsystem.entity.audit.DateAndTimeAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ExactColumns{
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private String id;

    private int bankomats;
    private int computers;
    private int keyboard;
    private int printer;
    private int mouse;
    private int monitor;
    private int employees;
    private LocalDate date;

    private String departmentId;

    private List<NewColumn> newColumns;
    @JsonIgnore
    private List<HistoryOfChanges> historyOfChanges;

//    @ElementCollection
//    private List<Long> newColumnsId;
}
