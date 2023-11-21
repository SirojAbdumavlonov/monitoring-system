package com.example.monitoringsystem.model;

import com.example.monitoringsystem.entity.Efficiency;
import com.example.monitoringsystem.entity.ExactColumns;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueWithEfficiency {
    ExactColumns values;
    Efficiency efficiency;
}
