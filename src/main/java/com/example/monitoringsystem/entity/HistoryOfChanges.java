package com.example.monitoringsystem.entity;

import com.example.monitoringsystem.entity.audit.DateAndTimeAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryOfChanges extends DateAndTimeAudit {

    private int oldValue;

    private int newValue;

    private String columnName;

    private String userId;//user who updated value
}
