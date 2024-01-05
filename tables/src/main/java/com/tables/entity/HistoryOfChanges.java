package com.tables.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryOfChanges{

    private Object oldValue;

    private Object newValue;

    private String columnName;

    private String userId;//user who updated value

    private LocalDateTime createdDateTime;
}
