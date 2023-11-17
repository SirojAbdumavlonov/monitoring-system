package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestType;
import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.*;
import com.example.monitoringsystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExactValuesService {
    private final ExactValuesRepository exactValuesRepository;
    private final ExactColumnsRepository exactColumnsRepository;
    private final NewColumnRepository newColumnRepository;
    private final DepartmentRepository departmentRepository;
    private final NewColumnToExactValuesRepository newColumnToExactValuesRepository;
    private final ExactColumnsMapper exactColumnsMapper;
    private final ColumnNamesRepository columnNamesRepository;
    private final UserRepository userRepository;
    private final HistoryOfChangesRepository historyOfChangesRepository;
    private final RequestForChangingValueRepository changingValueRepository;

    @Transactional
    public void saveData(AllColumns allColumns) {

        Department department =
                departmentRepository.findById(allColumns.getDepartmentId()).orElseThrow(() -> new BadRequestException("Not found!"));

        List<NewColumnsToExactValue> newColumns = new ArrayList<>();
        if(allColumns.getNewColumns() != null){
            for (NewColumnModel model : allColumns.getNewColumns()) {
                NewColumnsToExactValue newColumn = NewColumnsToExactValue
                        .builder()
                        .value(model.getValue())
                        .name(model.getColumnName())
                        .department(department)
                        .build();
                newColumnToExactValuesRepository.save(newColumn);
            }
        }

        ExactValues exactValues = ExactValues
                .builder()
                .bankomats(allColumns.getBankomats())
                .computers(allColumns.getComputers())
                .employees(allColumns.getEmployees())
                .monitor(allColumns.getMonitor())
                .mouse(allColumns.getMouse())
                .printer(allColumns.getPrinter())
                .build();
        exactValuesRepository.save(exactValues);
    }

    @Transactional
    public void saveDailyData(AllColumns allColumns) {

        Department department =
                departmentRepository.findById(allColumns.getDepartmentId())
                        .orElseThrow(() -> new BadRequestException("Not found!"));
        LocalDate today = LocalDate.now();

        for (NewColumnModel model : allColumns.getNewColumns()) {
            //Firstly, I check if I have a column with this name
            if(newColumnRepository.findByNameAndDepartmentId(model.getColumnName(),department.getId()).isPresent()){
                //Then, I find out and initiate it to its class
                NewColumn newColumn = newColumnRepository.findByNameAndDepartmentId
                        (model.getColumnName(),department.getId()).orElseThrow(()
                        -> new BadRequestException("Error in getting column"));
                //Get all values of column which name is in given class
                List<ValueWithDate> values = newColumn.getValues();
                //Add to table new value to new/old column
                values.add(new ValueWithDate(model.getValue(), today));
                //Change values of column to old list + new value
                newColumn.setValues(values);
                //Save column with new value
                newColumnRepository.save(newColumn);
            }
            else {
                NewColumn newColumn = NewColumn.builder()
                        .values(List.of(new ValueWithDate(model.getValue(), today)))
                        .name(model.getColumnName())
                        .department(department)
                        .build();
                newColumnRepository.save(newColumn);
            }
        }

        ExactColumns exactColumns = ExactColumns
                .builder()
                .bankomats(allColumns.getBankomats())
                .computers(allColumns.getComputers())
                .employees(allColumns.getEmployees())
                .monitor(allColumns.getMonitor())
                .mouse(allColumns.getMouse())
                .printer(allColumns.getPrinter())
                .department(department)
                .build();
        exactColumnsRepository.save(exactColumns);
    }
    public WholeDepartment<ExactColumns, List<NewColumn>> getValues(LocalDate today){

        ExactColumns exactColumns =
                exactColumnsRepository.findByCreatedDate(today);
        List<NewColumn> newColumn =
                newColumnRepository.findByDepartmentId(exactColumns.getDepartment().getId());

        return new WholeDepartment<>(exactColumns, newColumn);
    }
    public WholeDepartment<ExactValues, List<NewColumnsToExactValue>> getFixedValues(String departmentId) {
        ExactValues exactValues =
                exactValuesRepository.findByDepartmentId(departmentId);
        List<NewColumnsToExactValue> newColumnsToExactValueList =
                newColumnToExactValuesRepository.findByDepartmentId(departmentId);
        return new WholeDepartment<>(exactValues, newColumnsToExactValueList);
    }
    @Transactional
    public void updateColumns(UpdateRequest updateRequest, String departmentId, String currentUserId){


        LocalDate today = LocalDate.now();
        ExactColumnsDTO exactColumnsDTO =
                updateRequest.getExactColumns();//get all changed columns

        if(columnNamesRepository.existsByColumnName(updateRequest.getColumnName())) {//check if column was updated from main table
            ExactColumns exactColumns =
                    exactColumnsRepository.findById(exactColumnsDTO.getId()).orElseThrow(() -> new BadRequestException("Not found!"));
            //if it is in main table, change the value
            exactColumnsMapper.updateChangedColumn(exactColumnsDTO, exactColumns);
            //change the value
            exactColumnsRepository.save(exactColumns);
            //save it
        }
        else{
            NewColumn newColumn =
                    newColumnRepository.findByNameAndDepartment_Id
                            (updateRequest.getColumnName(), departmentId);
            //In this line of code, I know that it is newly added column, I want to find it in table of new columns
            //And update its value
            Integer newValue = updateRequest.getNewValue();
            Integer oldValue = updateRequest.getOldValue();
            for(ValueWithDate value: newColumn.getValues()){
                if (value.getValue().equals(oldValue) &&
                        value.getLocalDate().isEqual(today)){
                    value.setValue(newValue);
                }
            }

            newColumnRepository.save(newColumn);
        }
        Department department =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() -> new BadRequestException("Not found!"));
        Userr userr =
                userRepository.findById(currentUserId).
                        orElseThrow(() -> new BadRequestException("User not found!"));
        HistoryOfChanges historyOfChanges =
                HistoryOfChanges.builder()
                        .oldValue(updateRequest.getOldValue())
                        .newValue(updateRequest.getNewValue())
                        .columnName(updateRequest.getColumnName())
                        .department(department)
                        .userWhoUpdated(userr)
                        .build();
        historyOfChangesRepository.save(historyOfChanges);
    }

    public void requestForChangingFixedValue(RequestForFixedValueModel model, String userId) {
        RequestForFixedValue requestForFixedValue =
                RequestForFixedValue.builder()
                        .oldValue(model.getOldValue())
                        .newValue(model.getNewValue())
                        .message(model.getMessage())
                        .requestType(RequestType.FIXED_VALUE)
                        .adminId(userId)
                        .columnName(model.getColumnName())
                        .build();
        changingValueRepository.save(requestForFixedValue);

    }
}
