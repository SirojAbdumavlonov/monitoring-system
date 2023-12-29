package com.example.request.model;


import com.util.model.ChangedColumnWithMessage;

import java.util.List;

public record RequestForFixedValueModel<T>(List<ChangedColumnWithMessage<T>> changedColumnWithMessages,
                                        String requestType) {
}