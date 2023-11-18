package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.model.SignInRequest;
import com.example.monitoringsystem.model.SignUpRequest;
import com.example.monitoringsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    //    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.saveUser(signUpRequest);

        logger.info("No mistake while registering!");

        return ResponseEntity.ok("Registered successfully!");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {

        userService.signIn(signInRequest);

        logger.info("No mistake while signing in!");

        return ResponseEntity.ok("Data found!");
    }

//    @GetMapping("/requests")
//    public ResponseEntity<?> getAllRequests(){
//
//        List<RequestOfUser> getAllRequests =
//                requestsService.getAllRequest();
//
//         return ResponseEntity.ok(getAllRequests);
//    }

//    @PutMapping("/requests/{approvement}/{id}")
//    public ResponseEntity<?> acceptOrDeclineRequest(@PathVariable String approvement,
//                                                    @PathVariable String id,
//                                                    @RequestBody String reason){
//        if(approvement.equals("accept")){
//            userService.acceptRequest(id);
//        }
//        else{
//            userService.declineRequest(id, reason);
//        }
//
//        return null;
//    }


}
