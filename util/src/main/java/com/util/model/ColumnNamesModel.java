package com.util.model;

import java.util.List;

public record ColumnNamesModel(
        List<ChangedColumnWithMessage<Object>> changedColumnWithMessages) {

}
