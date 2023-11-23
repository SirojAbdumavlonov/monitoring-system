package com.example.monitoringsystem.entity;

import com.example.monitoringsystem.entity.audit.DateAndTimeAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TableChanges extends DateAndTimeAudit {
    private Object oldValue;

    private Object newValue;

    private String columnName;

    private String userId;//user who updated value

    private String type;

    private String departmentId;
}
