package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.model.SignInRequest;
import com.example.monitoringsystem.model.SignUpRequest;
import com.example.monitoringsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    //    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.saveUser(signUpRequest);

        return null;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        SignUpRequest signUpRequest = new SignUpRequest();
        boolean validName = signInRequest.getFullName().equals(signUpRequest.getFullName());
        boolean validPassword = signInRequest.getPassword().equals(signUpRequest.getPassword());


        if (validName && validPassword) {
            return ResponseEntity.ok("Sign-in successful");
        } else
            return ResponseEntity.badRequest().body("Invalid Username or Password");


    }


}
