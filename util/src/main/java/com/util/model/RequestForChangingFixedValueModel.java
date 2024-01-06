package com.util.model;

public record RequestForChangingFixedValueModel
        (RequestForFixedValueModel<Object> model,
         String userId, String departmentId) {
}
