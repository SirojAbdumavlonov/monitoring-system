package com.example.monitoringsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewColumnEfficiency {

    private String name;

    private List<ValueWithDate> list;


}
