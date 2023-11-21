package com.example.monitoringsystem.model;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
public class FromAndToDates {
    private LocalDate from;
    private LocalDate to;

    public FromAndToDates(){}
    public FromAndToDates(LocalDate from, LocalDate to){
        this.from = from;
        this.to = to;
    }
}
