package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestsService {
    private final RequestOfUserRepository request;
    public List<RequestOfUser> getAllRequest(){
        List<RequestOfUser> requests =
                request.findByStatus(RequestStatus.WAITING);
        return requests;
    }

}
