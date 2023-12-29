package com.example.monitoringsystem.controller;

import com.example.monitoringsystem.service.HelperService;
import com.util.tool.AcceptanceModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelperController {
    private final HelperService helperService;

    @PostMapping("/fixed-value/accept-request")
    public void acceptRequest(@RequestBody AcceptanceModel model){
        helperService.acceptRequest(model.requestType(),
                model.departmentId(), model.request(),
                model.changedColumnWithMessages(), model.userId());
    }

}
