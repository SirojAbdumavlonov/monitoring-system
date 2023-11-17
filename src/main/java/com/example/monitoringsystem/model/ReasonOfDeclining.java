package com.example.monitoringsystem.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReasonOfDeclining {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private String id;

    private String message;

    private String declinedUserId;

}
