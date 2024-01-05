package com.tables.service;

import com.tables.entity.ExactValues;
import com.tables.entity.NewColumnsToExactValue;
import com.tables.entity.TableChanges;
import com.tables.repository.TableChangesRepository;
import com.tables.repository.ExactValuesRepository;
import com.tables.tool.MapperProperties;
import com.util.constants.Localhost;
import com.util.model.*;
import com.util.constants.RequestType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HelperService {
    private final ExactValuesRepository exactValuesRepository;
    private final WebClient webClient;
    private final MapperProperties mapperProperties;
    private final TableChangesRepository changesRepository;

    public void acceptRequest(String requestType, String departmentId,
                              AcceptOrDeclineRequest request,
                              List<ChangedColumnWithMessage<Object>> changedColumnWithMessages,
                              String userId){
        if (requestType.equals(RequestType.FIXED_VALUE)){

            ExactValues exactValues = exactValuesRepository.
                    findByDepartmentId(departmentId);

            ExactValuesDTO exactValuesDTO = request.exactValuesDTO();
            log.info("exact = {}", exactValues);
            log.info("dto = {}", exactValuesDTO);

            for (ChangedColumnWithMessage<Object> changedColumnWIthMessage : changedColumnWithMessages) {
                boolean existColumnNames = webClient.post()
                        .uri(Localhost.REQUEST + "check-column-names")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ColumnNamesModel(changedColumnWithMessages))
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .subscribe().isDisposed();

                if (existColumnNames) {

                    mapperProperties.copyNonNullProperties(exactValuesDTO, exactValues);

                    log.info("exact values = {}", exactValues);
                } else {
                    if (exactValues.getNewColumnsToExactValueList() != null) {
                        for (NewColumnsToExactValue newColumn : exactValues.getNewColumnsToExactValueList()) {
                            if (newColumn.getName().equals(changedColumnWIthMessage.columnName())) {

                                newColumn.setValue((Integer) changedColumnWIthMessage.newValue());
                            }
                        }
                    }
                }
            }
            exactValuesRepository.save(exactValues);
        }
        else if(requestType.equals(RequestType.DEPARTMENT_VALUE)){

            webClient.post()
                    .uri(Localhost.DEPT + "update-save-department")
                    .bodyValue(new UpdatableDepartment(request.departmentDTO(), departmentId))
                    .retrieve()
                    .bodyToMono(Object.class)
                    .subscribe();
        }
        log.info("Changes started");

        for (ChangedColumnWithMessage changedColumnWIthMessage : changedColumnWithMessages) {
            TableChanges tableChanges =
                    TableChanges.builder()
                            .type(requestType)
                            .columnName(changedColumnWIthMessage.columnName())
                            .newValue(changedColumnWIthMessage.newValue())
                            .oldValue(changedColumnWIthMessage.oldValue())
                            .userId(userId)
                            .build();
            log.info("Table changes {}", tableChanges);
            changesRepository.save(tableChanges);
        }
    }
}
