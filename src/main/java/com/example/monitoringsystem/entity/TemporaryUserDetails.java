package com.example.monitoringsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemporaryUserDetails {
    @Id
    @GeneratedValue( // serial DB
            strategy = GenerationType.IDENTITY
    )
    private String id;
    private String fullName;
    private RoleName roleName;
    private String password;

    @ManyToOne
    @JoinColumn(
            name = "department_id"
    )
    private Department department;

    @OneToOne
    @JoinColumn(
            name = "request_id"
    )
    private RequestOfUser requestOfUser;
}
