package com.example.request.controller;

import com.example.request.service.HelperService;
import com.util.model.ColumnNamesModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelperController {
    private final HelperService helperService;

    @PostMapping("/check-column-names")
    public Boolean checkColumnNames(@RequestBody ColumnNamesModel columnNamesModel){
        return helperService.checkColumnNames(columnNamesModel);
    }

}
