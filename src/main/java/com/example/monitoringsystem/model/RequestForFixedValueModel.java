package com.example.monitoringsystem.model;


import java.util.List;

public record RequestForFixedValueModel<T>(List<ChangedColumnWithMessage<T>> changedColumnWithMessages,
                                        String requestType) {
}
