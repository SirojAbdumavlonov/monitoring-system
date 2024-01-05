package com.tables.service;

import com.tables.entity.ExactValues;
import com.tables.entity.NewColumnsToExactValue;
import com.tables.model.AllColumns;
import com.util.model.NewColumnModel;
import com.tables.repository.ExactValuesRepository;
import com.util.constants.Localhost;
import com.util.model.NewColumnsModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExactValuesService {
    private final ExactValuesRepository exactValuesRepository;
    private final WebClient webClient;


    @Transactional
    public void saveData(AllColumns allColumns, String departmentId) {

        Boolean existsDepartment = webClient.get()
                .uri(Localhost.DEPT + "check-dept/" + departmentId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        if(Boolean.FALSE.equals(existsDepartment)){
            return;
        }

        List<NewColumnsToExactValue> newColumns = new ArrayList<>();

        if(allColumns.newColumns() != null){

            webClient.post()
                    .uri(Localhost.REQUEST + "check-column-names-and-save")
                    .bodyValue(new NewColumnsModel(allColumns.newColumns()))
                    .retrieve()
                    .bodyToMono(Void.class);

            for (NewColumnModel model : allColumns.newColumns()) {
                NewColumnsToExactValue newColumn = NewColumnsToExactValue
                        .builder()
                        .value(model.value())
                        .name(model.columnName())
                        .build();
                newColumns.add(newColumn);

            }
        }

        ExactValues exactValues = ExactValues
                .builder()
                .bankomats(allColumns.bankomats())
                .computers(allColumns.computers())
                .employees(allColumns.employees())
                .monitor(allColumns.monitor())
                .mouse(allColumns.mouse())
                .printer(allColumns.printer())
                .newColumnsToExactValueList(newColumns)
                .departmentId(departmentId)
                .build();
        exactValuesRepository.save(exactValues);
    }

    //            //Firstly, I check if I have a column with this name
//            if(newColumnRepository.findByNameAndDepartmentId(model.getColumnName(),department.getId()).isPresent()){
//                //Then, I find out and initiate it to its class
//                NewColumn newColumn = newColumnRepository.findByNameAndDepartmentId
//                        (model.getColumnName(),department.getId()).orElseThrow(()
//                        -> new BadRequestException("Error in getting column"));
//                //Get all values of column which name is in given class
//                List<ValueWithDate> values = newColumn.getValues();
//                //Add to table new value to new/old column
//                values.add(new ValueWithDate(model.getValue(), today));
//                //Change values of column to old list + new value
//                newColumn.setValues(values);
//                //Save column with new value
//                newColumnRepository.save(newColumn);



}
