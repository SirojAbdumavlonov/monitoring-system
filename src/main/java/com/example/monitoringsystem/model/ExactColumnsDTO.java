package com.example.monitoringsystem.model;


public record ExactColumnsDTO(String id, Integer bankomats,
                              Integer computers, Integer keyboard, Integer printer, Integer mouse,
                              Integer monitor, Integer employees) {
}
