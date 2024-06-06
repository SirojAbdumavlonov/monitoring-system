package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.*;
import com.example.monitoringsystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
    public List<UserDataWithDepartment> getNecessaryDataAboutUsers(){
        List<Userr> allUsers = userRepository.findAll();
        List<UserDataWithDepartment> userDataWithDepartments =
                new ArrayList<>(allUsers.size());
        Department department;
        for (Userr user: allUsers){
            department =
                    departmentRepository.findById
                            (user.getDepartmentId()).orElseThrow(null);
            userDataWithDepartments.add(
                    new UserDataWithDepartment(user.getUserId(), user.getFullName(),
                            user.getRole(), department.getDepartmentName(), department.getAddress())
            );
        }
        return userDataWithDepartments;
    }
    public SearchingUserDataWithDepartmentResponse getSearchingResponse(
            String searchableWord) throws ExecutionException, InterruptedException {

        CompletableFuture<List<String>> userIds = CompletableFuture.supplyAsync(
                () -> {
                    List<Userr> userrList = userRepository.
                            findByUserIdContainingIgnoreCase(searchableWord);
                    return userrList.stream()
                            .map(Userr::getUserId)
                            .collect(Collectors.toList());
                }
        );
        CompletableFuture<List<String>> userFullNames = CompletableFuture.supplyAsync(
                () -> {
                    List<Userr> userrList = userRepository.
                            findByFullNameContainingIgnoreCase(searchableWord);
                    return userrList.stream()
                            .map(Userr::getUserId)
                            .collect(Collectors.toList());
                }
        );
        CompletableFuture<List<String>> departmentNames = CompletableFuture.supplyAsync(
                () -> {
                    List<Department> departmentList =
                            departmentRepository.
                                    findByDepartmentNameContainingIgnoreCase(searchableWord);
                    return departmentList.stream()
                            .map(Department::getDepartmentName)
                            .collect(Collectors.toList());
                }
        );

        CompletableFuture<Void> waitAllFutures =
                CompletableFuture.allOf(userIds, userFullNames, departmentNames);
        waitAllFutures.join();

        SearchingUserDataWithDepartmentResponse response =
                new SearchingUserDataWithDepartmentResponse(
                        userIds.get(),userFullNames.get(), departmentNames.get()
                );
        return response;
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
