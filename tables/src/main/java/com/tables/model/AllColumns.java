package com.tables.model;

import com.util.model.NewColumnModel;
import lombok.Builder;


import java.util.List;


@Builder
public record  AllColumns(int bankomats, int computers, int keyboard, int printer,int mouse,
                         int monitor, int employees, List<NewColumnModel> newColumns) {

}
