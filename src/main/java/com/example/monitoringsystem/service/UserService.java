package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.model.SignInRequest;
import com.example.monitoringsystem.model.SignUpRequest;
import com.example.monitoringsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DepartmentRepository departmentRepository;
    private final RequestOfUserRepository repository;
    private final TemporaryUserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReasonOfDecliningRepository decliningRepository;

    public void saveUser(SignUpRequest signUpRequest) {
        RequestOfUser requestOfUser = RequestOfUser.builder()
                .status(RequestStatus.WAITING)
                .build();
        Department department =
                departmentRepository.findByDepartmentName(
                        signUpRequest.getBranch()).orElseThrow(() -> new RuntimeException("No such branch"));


        TemporaryUserDetails userDetails = TemporaryUserDetails.builder()
                .fullName(signUpRequest.getFullName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roleName(RoleName.USER)
                .department(department)
                .requestOfUser(requestOfUser)
                .build();

        repository.save(requestOfUser);

        userDetailsRepository.save(userDetails);

    }
    public void signIn(SignInRequest signInRequest){
        Userr userr =
                userRepository.findById(signInRequest.getId()).orElseThrow(
                        () -> new RuntimeException("No such user with this id!")
                );
        if(!passwordEncoder.matches(userr.getPassword(), passwordEncoder.encode(signInRequest.getPassword()))){
            throw new RuntimeException("Incorrect user credentials");
        }
    }

    public void acceptRequest(Long id) {
        TemporaryUserDetails temporary = getTemporaryUserDetails(id);

        Userr user = Userr.builder()
                .department(temporary.getDepartment())
                .fullName(temporary.getFullName())
                .password(temporary.getPassword())
                .roleName(temporary.getRoleName())
                .requestOfUser(temporary.getRequestOfUser())
                .build();

        userRepository.save(user);
        userDetailsRepository.delete(temporary);

        repository.updateRequestOfStatus(
                RequestStatus.ACCEPTED, temporary.getRequestOfUser().getRequestId());
    }

    public void declineRequest(Long id, String reason) {
        TemporaryUserDetails temporary = getTemporaryUserDetails(id);

        ReasonOfDeclining reasonOfDeclining = ReasonOfDeclining.builder()
                .declinedUserId(id)
                .message(reason)
                .build();

        decliningRepository.save(reasonOfDeclining);

        repository.updateRequestOfStatus(
                RequestStatus.DECLINED, temporary.getRequestOfUser().getRequestId()
        );
    }

    private TemporaryUserDetails getTemporaryUserDetails(Long id) {
        TemporaryUserDetails temporary =
                userDetailsRepository.findById(id).orElseThrow(
                        ()-> new RuntimeException("No such user Found")
                );
        return temporary;
    }
}
