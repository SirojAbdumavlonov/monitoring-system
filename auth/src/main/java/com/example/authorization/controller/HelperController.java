package com.example.authorization.controller;

import com.example.authorization.entity.Userr;
import com.example.authorization.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelperController {
    private final UserService userService;
    @GetMapping("/user/{userId}")
    public Userr getUser(@PathVariable String userId){
        return userService.getUser(userId);
    }

}
