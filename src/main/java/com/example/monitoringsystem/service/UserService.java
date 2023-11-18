package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.model.ReasonOfDeclining;
import com.example.monitoringsystem.model.SignInRequest;
import com.example.monitoringsystem.model.SignUpRequest;
import com.example.monitoringsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DepartmentRepository departmentRepository;
    private final TemporaryUserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(SignUpRequest signUpRequest) {

        Department department =
                departmentRepository.findByDepartmentName(
                        signUpRequest.getDepartmentName()).orElseThrow(() -> new RuntimeException("No such branch"));

        Userr user = Userr.builder()
                .id(signUpRequest.getId())//id of user given by super admin
                .departmentId(department.getId())
                .fullName(signUpRequest.getFullName())
                .password(signUpRequest.getPassword())
                .roleName(RoleName.USER)
                .build();

        userRepository.save(user);
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
