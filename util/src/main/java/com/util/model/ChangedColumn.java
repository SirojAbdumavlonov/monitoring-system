package com.util.model;

public record ChangedColumn(String columnName,
                            Integer oldValue, Integer newValue) {
}
