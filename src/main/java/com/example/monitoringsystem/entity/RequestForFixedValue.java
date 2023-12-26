package com.example.monitoringsystem.entity;

import com.example.monitoringsystem.model.ChangedColumnWithMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@Builder
public class RequestForFixedValue{
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private String id;
    private String adminId;//Who requested

    @JsonIgnore
    private String requestType;

    private String reason;
    @JsonIgnore
    private String status;
    private String departmentId;

    private LocalDateTime localDateTime;

    private List<ChangedColumnWithMessage<Object>> changedColumnWithMessages;

}
