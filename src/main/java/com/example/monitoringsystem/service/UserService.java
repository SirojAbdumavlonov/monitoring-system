package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.entity.RequestOfUser;
import com.example.monitoringsystem.entity.RoleName;
import com.example.monitoringsystem.entity.TemporaryUserDetails;
import com.example.monitoringsystem.model.SignUpRequest;
import com.example.monitoringsystem.repository.DepartmentRepository;
import com.example.monitoringsystem.repository.RequestOfUserRepository;
import com.example.monitoringsystem.repository.TemporaryUserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DepartmentRepository departmentRepository;
    private final RequestOfUserRepository repository;
    private final TemporaryUserDetailsRepository userDetailsRepository;

    public void saveUser(SignUpRequest signUpRequest) {
        RequestOfUser requestOfUser = RequestOfUser.builder()
                .status(RequestStatus.WAITING)
                .build();
        Department department =
                departmentRepository.findByDepartmentName(
                        signUpRequest.getBranch()).orElseThrow(() -> new RuntimeException("No such branch"));

        TemporaryUserDetails userDetails = TemporaryUserDetails.builder()
                .fullName(signUpRequest.getFullName())
                .password(signUpRequest.getPassword())
                .roleName(RoleName.USER)
                .department(department)
                .requestOfUser(requestOfUser)
                .build();

        repository.save(requestOfUser);

        userDetailsRepository.save(userDetails);

    }
}
