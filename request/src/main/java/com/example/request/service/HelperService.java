package com.example.request.service;

import com.example.request.repository.ColumnNamesRepository;
import com.util.exception.BadRequestException;
import com.util.model.ChangedColumnWithMessage;
import com.util.model.ColumnNamesModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelperService {
    private final ColumnNamesRepository columnNamesRepository;
    public Boolean checkColumnNames(ColumnNamesModel columnNamesModel){

        for (ChangedColumnWithMessage<Object> changedColumnWIthMessage : columnNamesModel.changedColumnWithMessages()) {
            //todo: firstly I get all possible changes of row(e.g., computers and keyboards to 10)
            // So, I check has there such column
            if (!columnNamesRepository.existsByColumnName(changedColumnWIthMessage.columnName())) {
                throw new BadRequestException("There is no column with this name!");
            }
        }
        return true;
    }

}
