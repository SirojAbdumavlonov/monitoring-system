package com.tables.controller;

import com.util.constants.Localhost;
import com.util.model.ApiResponse;
import com.util.model.RequestForChangingFixedValueModel;
import com.util.model.RequestForFixedValueModel;
import com.util.security.CurrentUserId;
import com.tables.model.AllColumns;
import com.tables.service.ExactValuesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ExactValuesController {

    private final ExactValuesService exactValuesService;
    private final WebClient webClient;

    // fixed data
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER')")
    @PostMapping("/save/fixed-data/{departmentId}")
    public ResponseEntity<?> saveSettingsData(@RequestBody AllColumns allColumns,
                                              @PathVariable String departmentId) {

        exactValuesService.saveData(allColumns, departmentId);

        log.info("No mistake in saving fixed data!");

        return ResponseEntity.ok(new ApiResponse("Saved successfully!"));
    }

    // daily configuration of the table which is controlled by admins


    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    @PostMapping("/update/fixed-data/{departmentId}")
    public ResponseEntity<?> changeFixedValue(
            @CurrentUserId UserDetails user,
            @PathVariable String departmentId,
            @RequestBody RequestForFixedValueModel<Object> requestForFixedValue){

        //request type can be values which are in RequestType class
        String userId = user.getUsername();

        webClient.post()
                        .uri(Localhost.REQUEST + "send-request-to-save")
                                .bodyValue(new RequestForChangingFixedValueModel(
                                        requestForFixedValue,
                                        userId,
                                        departmentId
                                ))
                .retrieve()
                .bodyToMono(Void.class);

        return ResponseEntity.ok(new ApiResponse("Request sent successfully!"));
    }


}
