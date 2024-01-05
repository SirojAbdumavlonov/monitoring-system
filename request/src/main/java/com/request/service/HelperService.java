package com.request.service;

import com.request.entity.ColumnNames;
import com.request.repository.ColumnNamesRepository;
import com.util.exception.BadRequestException;
import com.util.model.ChangedColumnWithMessage;
import com.util.model.ColumnNamesModel;
import com.util.model.NewColumnModel;
import com.util.model.NewColumnsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelperService {
    private final ColumnNamesService columnNamesService;
    private final ColumnNamesRepository columnNamesRepository;
    public Boolean checkColumnNames(ColumnNamesModel columnNamesModel){

        for (ChangedColumnWithMessage<Object> changedColumnWIthMessage : columnNamesModel.changedColumnWithMessages()) {
            //todo: firstly I get all possible changes of row(e.g., computers and keyboards to 10)
            // So, I check has there such column
            if (!existsByColumnName(changedColumnWIthMessage.columnName())) {
                throw new BadRequestException("There is no column with this name!");
            }
        }
        return true;
    }
    public Boolean existsByColumnName(String columnName){
        return columnNamesService.getAsHashMap().containsKey(columnName);
    }
    public void checkColumnNameAndSave(NewColumnsModel newColumnsModel){
        for (NewColumnModel newColumnModel: newColumnsModel.newColumnModels()){
            if (!existsByColumnName(newColumnModel.columnName())){
                columnNamesRepository.save(new ColumnNames(newColumnModel.columnName()));
            }
        }
    }

}
