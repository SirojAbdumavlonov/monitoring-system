package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.model.UserDataWithDepartment;
import com.example.monitoringsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> getUsersDataWithDepartment(
            @RequestParam(required = false) String searchableWord,
//            @RequestParam(required = false) String userId,
//            @RequestParam(required = false) String fullName,
//            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false, defaultValue = "not-search") String apiOption) throws ExecutionException, InterruptedException
    //apiOption = search or not-search
    {
        if (apiOption.equals("search")){

            return ResponseEntity.ok(
                    userService.getSearchingResponse(searchableWord)
            );
        }

        List<UserDataWithDepartment> getUserDataWithDepartment =
                userService.getNecessaryDataAboutUsers();
        return ResponseEntity.ok(getUserDataWithDepartment);
    }

}
