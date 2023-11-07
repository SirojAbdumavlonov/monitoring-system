package com.example.monitoringsystem.entity;

import com.example.monitoringsystem.entity.audit.DateAndTimeAudit;
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
public class HistoryOfChanges extends DateAndTimeAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int oldValue;
    private int newValue;
    private String columnName;

    @ManyToOne
    @JoinColumn(
            name = "user"
    )
    private Userr userWhoUpdated;

    @ManyToOne
    @JoinColumn(
            name = "deparment_id"
    )
    private Department department;
}
