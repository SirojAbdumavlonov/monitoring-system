package com.example.monitoringsystem.entity.audit;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdDate","createdTime"},
        allowGetters = true
)
public abstract class DateAndTimeAudit implements Serializable {

    @CreatedDate
    @JsonIgnore
    @Indexed
    private LocalDate createdDate;

    @CreationTimestamp
    @JsonIgnore
    private LocalTime createdTime;
}
