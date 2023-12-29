package com.example.request.controller;

import com.util.model.AcceptOrDeclineRequest;
import com.example.request.model.RequestForFixedValueModel;
import com.example.request.service.RequestsService;
import com.util.model.ApiResponse;
import com.util.security.CurrentUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestsService requestsService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/request")
    public ResponseEntity<?> getAllRequests(@RequestParam(name = "option", defaultValue = "waiting") String option){
        //show requests which are in waiting status
        if(option.equals("all")){
            return ResponseEntity.ok(requestsService.getAllValues());
        }

        return ResponseEntity.ok(requestsService.getValues(option.toUpperCase()));
    }
    //todo: request is sent when user wants to change data of department

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
            response = requestsService.acceptRequest(requestId, request);
        }
        else if(option.equals("decline")){
            response = requestsService.declineRequest(requestId, request.reason());
        }
        return response;
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/update/add-info/{departmentId}")
    public ResponseEntity<?> changeDepartmentValue(
            @CurrentUserId UserDetails user,
            @PathVariable String departmentId,
            @RequestBody RequestForFixedValueModel<Object> requestForFixedValue){

        //request type can be values which are in RequestType class
        requestsService.requestForChangingFixedValue
                (requestForFixedValue, user.getUsername(), departmentId);

        return ResponseEntity.ok(new ApiResponse("Request sent successfully!"));
    }


}
