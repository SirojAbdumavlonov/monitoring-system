package com.tables.model;

import com.tables.entity.Efficiency;
import com.tables.entity.ExactColumns;


public record ValueWithEfficiency(ExactColumns values, Efficiency efficiency) {
}
