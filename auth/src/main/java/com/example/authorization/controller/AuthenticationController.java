package com.example.authorization.controller;

import com.example.authorization.service.UserService;
import com.example.authorization.model.AuthenticationResponse;
import com.example.authorization.model.SignInRequest;
import com.example.authorization.model.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;


    //todo: Function is tested and works
    //    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {

        AuthenticationResponse authenticationResponse =
                userService.saveUser(signUpRequest);

        log.info("Registered successfully!");
        //todo: registering with unique id
        // already used id is working
        // other fillings are checked by client side
        return ResponseEntity.ok(authenticationResponse);
    }
    //todo: Function is tested and works
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {

       AuthenticationResponse authenticationResponse =
                userService.signIn(signInRequest);

        log.info("Signed in successfully!");
        //todo: signing in only with id and password
        // trying in both ways(if id or password is incorrect)
        // incorrect password is working
        // incorrect id is working
        return ResponseEntity.ok(authenticationResponse);
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

//    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
//    @PutMapping("/update/role")
//    public ResponseEntity<?> updateUserRole(@RequestParam(name = "user-id") String userId,
//                                            @RequestBody String role){
//
//
//        return null;
//    }


}
