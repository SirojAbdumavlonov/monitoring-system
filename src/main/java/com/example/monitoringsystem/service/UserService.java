package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.entity.RequestOfUser;
import com.example.monitoringsystem.entity.RoleName;
import com.example.monitoringsystem.entity.TemporaryUserDetails;
import com.example.monitoringsystem.model.SignUpRequest;
import com.example.monitoringsystem.repository.DepartmentRepository;
import com.example.monitoringsystem.repository.TempRepositoryForDB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DepartmentRepository departmentRepository;

    public void saveUser(SignUpRequest signUpRequest) {
        RequestOfUser requestOfUser = RequestOfUser.builder()
                .status(RequestStatus.WAITING)
                .build();
        Department department = departmentRepository.findByDepartmentName(signUpRequest.getBranch());

        TemporaryUserDetails userDetails = TemporaryUserDetails.builder()
                .fullName(signUpRequest.getFullName())
                .password(signUpRequest.getPassword())
                .roleName(RoleName.USER)
                .department(department)
                .requestOfUser(requestOfUser)
                .build();



    }
}
