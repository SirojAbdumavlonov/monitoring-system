package com.tables.model;

import lombok.Builder;

@Builder
public record Requests(String fullName, String id, String department) {

}
