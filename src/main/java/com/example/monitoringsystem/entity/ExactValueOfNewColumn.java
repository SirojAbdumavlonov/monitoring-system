package com.example.monitoringsystem.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExactValueOfNewColumn {

    private String name;

    private int value;
}
