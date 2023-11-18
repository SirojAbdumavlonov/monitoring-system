package com.example.monitoringsystem.entity;

import com.example.monitoringsystem.entity.audit.DateTimeAudit;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class RequestForFixedValue extends DateTimeAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String adminId;//Who requested
    private String columnName;
    private Object oldValue;
    private Object newValue;
    private String requestType;
    private String message;
    private String reason;
    private String status;

}
