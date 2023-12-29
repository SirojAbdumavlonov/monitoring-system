package com.example.authorization.service;

import com.example.authorization.entity.Userr;
import com.example.authorization.repository.UserRepository;
import com.example.authorization.exception.BadRequestException;
import com.example.authorization.model.AuthenticationResponse;
import com.example.authorization.model.SignInRequest;
import com.example.authorization.model.SignUpRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;
    private final WebClient webClient;

    @Transactional
    public AuthenticationResponse saveUser(SignUpRequest signUpRequest) {

        String departmentId = webClient.get()
                .uri("http://localhost:8083/dept-name/" + signUpRequest.departmentName())
                .retrieve()
                .bodyToMono(String.class)
                .block();


        if(userRepository.existsByUserId(signUpRequest.userId())){
           throw new BadRequestException("This id has already been taken!");
        }



        Userr user = Userr.builder()
                .userId(signUpRequest.userId())//id of user given by super admin
                .departmentId(departmentId)
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
    public Userr getUser(String userId){
        return userRepository.findByUserId(userId).
                orElseThrow(() ->
                        new BadRequestException("User not found!"));
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
