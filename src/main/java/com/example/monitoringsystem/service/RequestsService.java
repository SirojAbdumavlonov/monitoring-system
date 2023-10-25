package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
import com.example.monitoringsystem.entity.RequestOfUser;
import com.example.monitoringsystem.model.Requests;
import com.example.monitoringsystem.repository.RequestOfUserRepository;
import jakarta.security.auth.message.callback.SecretKeyCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
