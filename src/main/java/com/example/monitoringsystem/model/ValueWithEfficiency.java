package com.example.monitoringsystem.model;

import com.example.monitoringsystem.entity.Efficiency;
import com.example.monitoringsystem.entity.ExactColumns;


public record ValueWithEfficiency(ExactColumns values, Efficiency efficiency) {
}
