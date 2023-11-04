package com.example.monitoringsystem.entity;

import com.example.monitoringsystem.entity.audit.DateAudit;
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
public class HistoryOfChanges extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oldValue;
    private String newValue;

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
