package com.request.service;

import com.request.entity.ColumnNames;
import com.request.repository.ColumnNamesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColumnNamesService {
    private final ColumnNamesRepository columnNamesRepository;

    @Cacheable(value = "ColumnNames")
    public HashMap<String, ColumnNames> getAsHashMap() {
        List<ColumnNames> entities = columnNamesRepository.findAll();

        HashMap<String, ColumnNames> resultMap = new HashMap<>();
        for (ColumnNames entity : entities) {
            // Assuming that columnName is unique, use the appropriate field as the key
            resultMap.put(entity.getColumnName(), entity);
        }

        return resultMap;
    }


}
