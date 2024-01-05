package com.tables.model;


import com.util.model.ChangedColumn;
import com.util.model.ExactColumnsDTO;

import java.time.LocalDate;
import java.util.List;


public record UpdateRequest(ExactColumnsDTO exactColumns,
                            List<ChangedColumn> changedColumns, LocalDate date) {
}
