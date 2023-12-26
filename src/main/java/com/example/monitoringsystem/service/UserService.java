package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.AuthenticationResponse;
import com.example.monitoringsystem.model.SignInRequest;
import com.example.monitoringsystem.model.SignUpRequest;
import com.example.monitoringsystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;


    @Transactional
    public AuthenticationResponse saveUser(SignUpRequest signUpRequest) {

        Department department =
                departmentRepository.findByDepartmentName(
                        signUpRequest.departmentName())
                        .orElseThrow(() -> new BadRequestException("No such branch!"));

        if(userRepository.existsByUserId(signUpRequest.userId())){
           throw new BadRequestException("This id has already been taken!");
        }



        Userr user = Userr.builder()
                .userId(signUpRequest.userId())//id of user given by super admin
                .departmentId(department.getId())
                .fullName(signUpRequest.fullName())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .role(signUpRequest.role())
                .build();

        userRepository.save(user);
        System.out.println("user = " + user);
        return new AuthenticationResponse(
                jwtService.generateToken(user)
        );
    }
    @Transactional
    public AuthenticationResponse signIn(SignInRequest signInRequest){

        Userr user =
                userRepository.findByUserId(signInRequest.userId()).orElseThrow(
                        () -> new BadRequestException("No such user with this id!")
                );
//        if(!passwordEncoder.matches(user.getPassword(), passwordEncoder.encode(signInRequest.password()))){
//            throw new BadRequestException("Incorrect password!");
//        }

        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.userId(),
                        signInRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("user = " + user);
        return new AuthenticationResponse(
                jwtService.generateToken(user)
        );
    }

//    public void acceptRequest(String id) {
//        TemporaryUserDetails temporary = getTemporaryUserDetails(id);
//
//        Userr user = Userr.builder()
//                .department(temporary.getDepartment())
//                .fullName(temporary.getFullName())
//                .password(temporary.getPassword())
//                .roleName(temporary.getRoleName())
//                .requestOfUser(temporary.getRequestOfUser())
//                .build();
//
//        userRepository.save(user);
//        userDetailsRepository.delete(temporary);
//    }

//    public void declineRequest(String id, String reason) {
//        TemporaryUserDetails temporary = getTemporaryUserDetails(id);
//
//        ReasonOfDeclining reasonOfDeclining = ReasonOfDeclining.builder()
//                .declinedUserId(id)
//                .message(reason)
//                .build();
//
//        decliningRepository.save(reasonOfDeclining);
//
//        repository.updateRequestOfStatus(
//                RequestStatus.DECLINED, temporary.getRequestOfUser().getRequestId()
//        );
//    }

//    private TemporaryUserDetails getTemporaryUserDetails(String id) {
//        TemporaryUserDetails temporary =
//                userDetailsRepository.findById(id).orElseThrow(
//                        ()-> new RuntimeException("No such user Found")
//                );
//        return temporary;
//    }
}
