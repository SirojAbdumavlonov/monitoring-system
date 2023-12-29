package com.util.tool;

import com.util.model.AcceptOrDeclineRequest;
import com.util.model.ChangedColumnWithMessage;

import java.util.List;

public record AcceptanceModel(String requestType, String departmentId,
                              AcceptOrDeclineRequest request,
                              List<ChangedColumnWithMessage<Object>> changedColumnWithMessages,
                              String userId) {
}
