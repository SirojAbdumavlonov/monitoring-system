package com.request.controller;

import com.request.entity.ColumnNames;
import com.request.service.ColumnNamesService;
import com.request.service.HelperService;
import com.request.service.RequestsService;
import com.util.model.ColumnNamesModel;
import com.util.model.NewColumnsModel;
import com.util.model.RequestForChangingFixedValueModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class HelperController {
    private final HelperService helperService;
    private final ColumnNamesService columnNamesService;
    private final RequestsService requestsService;
    @PostMapping("/check-column-names")
    public Boolean checkColumnNames(@RequestBody ColumnNamesModel columnNamesModel){
        return helperService.checkColumnNames(columnNamesModel);
    }
    @GetMapping("/check-column-names-changed-column")
    public HashMap<String, ColumnNames> checkColumnNamesOfUpdatedColumns() {
        return columnNamesService.getAsHashMap();
    }

    @PostMapping("/check-column-names-and-save")
    public void checkColumnNameAndSave(@RequestBody NewColumnsModel newColumnsModel){
        helperService.checkColumnNameAndSave(newColumnsModel);
    }
    @PostMapping("/send-request-to-save")
    public void sendRequestForSaving(@RequestBody RequestForChangingFixedValueModel request){
        requestsService.requestForChangingFixedValue(
                request.model(), request.userId(), request.departmentId());
    }
}
