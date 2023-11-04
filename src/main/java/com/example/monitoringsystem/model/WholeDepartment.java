package com.example.monitoringsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WholeDepartment<T, I> {
    private T columns;
    private I newColumns;
}
