package com.example.monitoringsystem.service;

import com.example.monitoringsystem.AbstractContainerBaseTest;
import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.entity.Location;
import com.example.monitoringsystem.entity.RoleName;
import com.example.monitoringsystem.entity.Userr;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.SignUpRequest;
import com.example.monitoringsystem.repository.DepartmentRepository;
import com.example.monitoringsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends AbstractContainerBaseTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DepartmentRepository departmentRepository;


    private UserService userService;

    @BeforeEach
    void setUp(){
        userService = new UserService(departmentRepository, userRepository, passwordEncoder);
    }
    @Test
    void registerUser(){
        Location location = Location.builder()
            .lat(22.5)
            .lon(33.4)
            .build();
        Department savedDepartment =
                Department.builder()
                        .id("100")
                        .departmentName("Chilonzor")
                        .idOfMainBranch(null)
                        .address("Chilonzor")
                        .location(location)
                        .build();
        departmentRepository.save(savedDepartment);

        SignUpRequest signUpRequest =
                SignUpRequest.builder()
                        .id("150")
                        .departmentName("Chilonzor")
                        .fullName("Sirojiddin Abdumavlonov")
                        .password("Siroj12@")
                        .build();
        userService.saveUser(signUpRequest);

        Department department =
                departmentRepository.findByDepartmentName(signUpRequest.getDepartmentName())
                        .orElseThrow(() -> new BadRequestException("Not found!"));

        Userr userr =
                Userr.builder()
                        .fullName(signUpRequest.getFullName())
                        .id(signUpRequest.getId())
                        .departmentId(department.getId())
                        .roleName(RoleName.USER)
                        .password(signUpRequest.getPassword())
                        .build();

        verify(userRepository).save(userr);

    }


}
