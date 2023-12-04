package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.RequestForFixedValue;
import com.example.monitoringsystem.repository.RequestForChangingValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestsService {
    private final RequestForChangingValueRepository valueRepository;

    public List<RequestForFixedValue> getValues(String option){
        return valueRepository.findByStatus(option);
    }
    public List<RequestForFixedValue> getAllValues(){
        return valueRepository.findAllByOrderByCreatedDateTimeDesc();
    }

}
