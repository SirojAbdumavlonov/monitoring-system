package com.example.monitoringsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOfUser {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long requestId;

    private String status;
}
