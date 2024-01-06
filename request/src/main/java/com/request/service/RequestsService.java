package com.request.service;

import com.request.entity.RequestForFixedValue;
import com.util.model.AcceptOrDeclineRequest;
import com.util.model.RequestForFixedValueModel;
import com.request.repository.RequestForChangingValueRepository;
import com.util.constants.Localhost;
import com.util.constants.RequestStatus;
import com.util.constants.RequestType;
import com.util.exception.BadRequestException;
import com.util.model.ApiResponse;
import com.util.model.ChangedColumnWithMessage;
import com.tables.model.AcceptanceModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestsService {
    private final RequestForChangingValueRepository forChangingValueRepository;
    private final WebClient webClient;
    private final HelperService helperService;


    public List<RequestForFixedValue> getValues(String option){
        return forChangingValueRepository.findByStatusOrderByLocalDateTimeDesc(option);
    }
    public List<RequestForFixedValue> getAllValues(){
        return forChangingValueRepository.findAllByOrderByLocalDateTimeDesc();
    }

    @Transactional
    public void requestForChangingFixedValue(
            RequestForFixedValueModel<Object> model, String userId, String departmentId) {

        Boolean existsDepartment = webClient.get()
                .uri(Localhost.DEPT + "check-dept/" + departmentId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();


        if(Boolean.FALSE.equals(existsDepartment)){
            throw new BadRequestException("Department not found!");
        }

        List<ChangedColumnWithMessage<Object>> changedColumnWithMessages = model.changedColumnWithMessages();

        if(model.requestType().equals(RequestType.FIXED_VALUE)) {


            for (ChangedColumnWithMessage<Object> changedColumnWIthMessage : changedColumnWithMessages) {
                //todo: firstly I get all possible changes of row(e.g., computers and keyboards to 10)
                // So, I check has there such column
                if (!helperService.existsByColumnName(changedColumnWIthMessage.columnName())) {
                    throw new BadRequestException("There is no column with this name!");
                }

            }
        }

        RequestForFixedValue requestForFixedValue =
                RequestForFixedValue.builder()
                        .changedColumnWithMessages(changedColumnWithMessages)
                        .requestType(model.requestType())
                        .adminId(userId)
                        .status(RequestStatus.WAITING)
                        .departmentId(departmentId)
                        .localDateTime(LocalDateTime.now())
                        .build();

        forChangingValueRepository.save(requestForFixedValue);
    }

    @Transactional
    public ResponseEntity<?> acceptRequest(String requestId, AcceptOrDeclineRequest request) {

        RequestForFixedValue requestForFixedValue =
                forChangingValueRepository.findById(requestId)
                        .orElseThrow(() -> new BadRequestException("Request not found!"));

        requestForFixedValue.setStatus(RequestStatus.ACCEPTED);

        //todo: I should send departmentId
        // changedColumnsMessages
        // sending a request for saving and updating values
        webClient.post()
                        .uri(Localhost.TABLES + "fixed-value/accept-request")
                                .bodyValue(new AcceptanceModel(
                                        requestForFixedValue.getRequestType(),
                                        requestForFixedValue.getDepartmentId(),
                                        request, requestForFixedValue.getChangedColumnWithMessages(),
                                        requestForFixedValue.getAdminId()
                                ))
                        .retrieve()
                                .toBodilessEntity()
                                        .block();

        forChangingValueRepository.save(requestForFixedValue);

        return ResponseEntity.ok(new ApiResponse("Accepted successfully!"));
    }
    @Transactional
    public ResponseEntity<?> declineRequest(String requestId, String reason) {
        RequestForFixedValue requestForFixedValue =
                forChangingValueRepository.findById(requestId)
                        .orElseThrow(() -> new BadRequestException("Request not found!"));

        requestForFixedValue.setStatus(RequestStatus.DECLINED);
        //todo: give a reason if request is declined
        requestForFixedValue.setReason(reason);

        forChangingValueRepository.save(requestForFixedValue);
        return ResponseEntity.ok(new ApiResponse("Request was declined!"));
    }

}
