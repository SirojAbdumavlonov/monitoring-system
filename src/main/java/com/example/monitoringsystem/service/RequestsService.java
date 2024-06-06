package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
import com.example.monitoringsystem.constants.RequestType;
import com.example.monitoringsystem.entity.Request;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.ChangedColumnWithMessage;
import com.example.monitoringsystem.model.RequestForFixedValueModel;
import com.example.monitoringsystem.repository.ColumnNamesRepository;
import com.example.monitoringsystem.repository.DepartmentRepository;
import com.example.monitoringsystem.repository.RequestForChangingValueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestsService {
    private final RequestForChangingValueRepository valueRepository;
    private final RequestForChangingValueRepository changingValueRepository;
    private final DepartmentRepository departmentRepository;
    private final ColumnNamesRepository columnNamesRepository;


    public List<Request> getValues(String option){
        return valueRepository.findByStatusOrderByLocalDateTimeDesc(option);
    }
    public List<Request> getAllValues(){
        return valueRepository.findAllByOrderByLocalDateTimeDesc();
    }

    @Transactional
    public void requestForChangingFixedValue(
            RequestForFixedValueModel<Object> model, String userId, String departmentId) {

        if(!departmentRepository.existsById(departmentId)){
            throw new BadRequestException("Department not found!");
        }

        List<ChangedColumnWithMessage<Object>> changedColumnWithMessages = model.changedColumnWithMessages();

        if(model.requestType().equals(RequestType.FIXED_VALUE)) {


            for (ChangedColumnWithMessage<Object> changedColumnWIthMessage : changedColumnWithMessages) {
                //todo: firstly I get all possible changes of row(e.g., computers and keyboards to 10)
                // So, I check has there such column
                if (!columnNamesRepository.existsByColumnName(changedColumnWIthMessage.columnName())) {
                    throw new BadRequestException("There is no column with this name!");
                }

            }
        }

        Request request =
                Request.builder()
                        .changedColumnWithMessages(changedColumnWithMessages)
                        .requestType(model.requestType())
                        .adminId(userId)
                        .status(RequestStatus.WAITING)
                        .departmentId(departmentId)
                        .localDateTime(LocalDateTime.now())
                        .build();

        changingValueRepository.save(request);
    }

}
