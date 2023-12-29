package com.util.model;

public record ChangedColumnWithMessage<T>(String columnName,
                                       T oldValue, T newValue, String message) {
}
