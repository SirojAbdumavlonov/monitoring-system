package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.entity.RequestOfUser;
import com.example.monitoringsystem.model.Requests;
import com.example.monitoringsystem.model.SignInRequest;
import com.example.monitoringsystem.model.SignUpRequest;
import com.example.monitoringsystem.service.RequestsService;
import com.example.monitoringsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final RequestsService requestsService;

    //    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.saveUser(signUpRequest);

        return null;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {

        userService.signIn(signInRequest);

        return ResponseEntity.ok("Data found!");

    }

    @GetMapping("/requests")
    public ResponseEntity<?> getAllRequests(){

        List<RequestOfUser> getAllRequests =
                requestsService.getAllRequest();

         return ResponseEntity.ok(getAllRequests);
    }

    @PutMapping("/requests/{approvement}/{id}")
    public ResponseEntity<?> acceptOrDeclineRequest(@PathVariable String approvement,
                                                    @PathVariable Long id,
                                                    @RequestBody String reason){
        if(approvement.equals("accept")){
            userService.acceptRequest(id);
        }
        else{
            userService.declineRequest(id, reason);
        }

        return null;
    }
}
