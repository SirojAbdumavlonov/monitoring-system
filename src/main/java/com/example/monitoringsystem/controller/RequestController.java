package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.model.AcceptOrDeclineRequest;
import com.example.monitoringsystem.service.DepartmentService;
import com.example.monitoringsystem.service.RequestsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestsService requestsService;
    private final DepartmentService departmentService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/request")
    public ResponseEntity<?> getAllRequests(@RequestParam(name = "option", defaultValue = "waiting") String option){
        //show requests which are in waiting status
        if(option.equals("all")){
            return ResponseEntity.ok(requestsService.getAllValues());
        }

        return ResponseEntity.ok(requestsService.getValues(option.toUpperCase()));
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/request")
    public ResponseEntity<?> acceptOrDeclineRequest(@RequestParam(name = "request-id") String requestId,
                                                    @RequestParam(name = "option") String option,
                                                    @RequestBody AcceptOrDeclineRequest request){
        ResponseEntity<?> response = null;
        log.info("request id = {}", requestId);
        log.info("option = {}", option);
        log.info("request = {}", request);
        //Option is used for accepting or declining request
        //Option can be only accept or decline
        if(option.equals("accept")){
            //request type can be only dep or fixed
            response = departmentService.acceptRequest(requestId, request);
        }
        else if(option.equals("decline")){
            response = departmentService.declineRequest(requestId, request.reason());
        }
        return response;
    }


}
