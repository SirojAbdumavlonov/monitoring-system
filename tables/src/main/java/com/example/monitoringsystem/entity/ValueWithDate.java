package com.example.monitoringsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;

@Embeddable
@Data
public class ValueWithDate {
    @Column(name = "value")
    private Integer value;
    @Column(name = "date")
    @Indexed
    private LocalDate localDate;

    public ValueWithDate(){}
    public ValueWithDate(Integer value, LocalDate date){
        this.localDate = date;
        this.value = value;
    }

}
