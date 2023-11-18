package com.example.monitoringsystem.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    double lon;
    double lat;
}
