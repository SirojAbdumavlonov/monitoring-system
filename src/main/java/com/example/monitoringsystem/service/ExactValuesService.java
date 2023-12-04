package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.RequestStatus;
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
    private final ColumnNamesRepository columnNamesRepository;
    private final UserRepository userRepository;
    private final RequestForChangingValueRepository changingValueRepository;
    private final EfficiencyRepository efficiencyRepository;
    private ColumnsMapper exactColumnsMapper;


    @Transactional
    public void saveData(AllColumns allColumns) {

        List<NewColumnsToExactValue> newColumns = new ArrayList<>();

        if(allColumns.newColumns() != null){
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

    @Transactional
    public Object saveDailyData(AllColumns allColumns) {

        List<NewColumn> columns = null;

        ExactValues exactValues =
                exactValuesRepository.findByDepartmentId(allColumns.departmentId());

        List<NewColumnsToExactValue> exactValuesList =
                exactValues.getNewColumnsToExactValueList();
        List<NewColumnEfficiency> efficiencyList = null;

        LocalDate today = LocalDate.now();
        Double newColumnsEfficiency = null;
        if (!allColumns.newColumns().isEmpty()) {
            columns = new ArrayList<>();
            for (NewColumnModel model : allColumns.newColumns()) {
                columns.add(
                        NewColumn.builder()
                                .name(model.columnName())
                                .value(model.value())
                                .build()
                );
            }

            Double helper = null;
            for (NewColumnsToExactValue newColumnsToExactValue : exactValuesList) {
                for (NewColumn newColumn : columns) {
                    if (newColumnsToExactValue.getName().equals(newColumn.getName())) {
                        helper = getEfficiency(newColumn.getValue(), newColumnsToExactValue.getValue());
//                        assert false;
                        efficiencyList.add(
                                NewColumnEfficiency.builder()
                                        .name(newColumn.getName())
                                        .value(helper)
                                        .build()
                        );
                        newColumnsEfficiency += helper;
                        helper = (double) 0;
                    }
                }
            }
        }

        //Find all fixed values of chosen department

        Efficiency efficiency =
                Efficiency.builder()
                        .bankomats(getEfficiency(allColumns.bankomats(), exactValues.getBankomats()))
                        .monitor(getEfficiency(allColumns.monitor(), exactValues.getMonitor()))
                        .mouse(getEfficiency(allColumns.monitor(), exactValues.getMouse()))
                        .computers(getEfficiency(allColumns.computers(), exactValues.getComputers()))
                        .printer(getEfficiency(allColumns.printer(), exactValues.getPrinter()))
                        .employees(getEfficiency(allColumns.employees(), exactValues.getEmployees()))
                        .efficiencyList(efficiencyList)
                        .totalEfficiency(getTotalEfficiency(allColumns, exactValues, newColumnsEfficiency))
                        .build();

        ExactColumns exactColumns = ExactColumns.builder()
                .bankomats(allColumns.bankomats())
                .computers(allColumns.computers())
                .mouse(allColumns.mouse())
                .monitor(allColumns.monitor())
                .printer(allColumns.printer())
                .keyboard(allColumns.keyboard())
                .newColumns(columns)
                .date(today)
                .build();

        efficiencyRepository.save(efficiency);

        exactColumnsRepository.save(exactColumns);

        return new WholeDepartment<>(exactColumns, efficiency);
    }

    private Double getEfficiency(int dailyData, int fixedData){
        return (double) (dailyData / fixedData) * 100;
    }
    private Double getTotalEfficiency(AllColumns allColumns, ExactValues exactValues, Double newColumnsEfficiency){
        return (
                getEfficiency(allColumns.bankomats(), exactValues.getBankomats()) +
                        getEfficiency(allColumns.monitor(), exactValues.getMonitor()) +
                        getEfficiency(allColumns.mouse(), exactValues.getMouse()) +
                        getEfficiency(allColumns.computers(), exactValues.getComputers()) +
                        getEfficiency(allColumns.printer(), exactValues.getPrinter()) +
                        getEfficiency(allColumns.employees(), exactValues.getEmployees()) +
                        newColumnsEfficiency
        );
    }



//    public WholeDepartment<ExactColumns, List<NewColumn>> getValues(LocalDate today){
//
//        ExactColumns exactColumns =
//                exactColumnsRepository.findByCreatedDate(today);
//
//        return new WholeDepartment<>(exactColumns, exactColumns.getNewColumns());
//    }
//    public WholeDepartment<ExactValues, List<NewColumnsToExactValue>> getFixedValues(String departmentId) {
//        ExactValues exactValues =
//                exactValuesRepository.findByDepartmentId(departmentId);
//
//        return new WholeDepartment<>(exactValues, exactValues.getNewColumnsToExactValueList());
//    }
    @Transactional
    public void updateColumns(UpdateRequest updateRequest, String departmentId, String currentUserId){

        ExactColumns exactColumns =
                exactColumnsRepository.findByCreatedDateAndDepartmentId(updateRequest.date(), departmentId)
                        .orElseThrow(() -> new BadRequestException("Error in finding table of department!"));

        ExactColumnsDTO exactColumnsDTO =
                updateRequest.exactColumns();//get all changed columns

        if(columnNamesRepository.existsByColumnName(updateRequest.columnName())) {//check if column was updated from main table
            //if it is in main table, change the value
            exactColumnsMapper.updateChangedColumn(exactColumnsDTO, exactColumns);
            //change the value
        }
        else{
            if(!exactColumns.getNewColumns().isEmpty()) {
                for (NewColumn newColumn : exactColumns.getNewColumns()) {
                    if (newColumn.getName().equals(updateRequest.columnName())) {
                        newColumn.setValue(updateRequest.newValue());
                    }
                }
            }
            //In this line of code, I know that it is newly added column, I want to find it in table of new columns
            //And update its value
//            Integer newValue = updateRequest.getNewValue();
//            Integer oldValue = updateRequest.getOldValue();
//            for(ValueWithDate value: newColumn.getValues()){
//                if (value.getValue().equals(oldValue) &&
//                        value.getLocalDate().isEqual(today)){
//                    value.setValue(newValue);
//                }
//            }

        }
        List<HistoryOfChanges> historyOfChangesList = null;

        Userr userr =
                userRepository.findById(currentUserId).
                        orElseThrow(() -> new BadRequestException("User not found!"));
        if(exactColumns.getHistoryOfChanges().isEmpty()){
            historyOfChangesList = new ArrayList<>();
        }else {
            historyOfChangesList = exactColumns.getHistoryOfChanges();
        }

        HistoryOfChanges historyOfChanges =
                HistoryOfChanges.builder()
                        .oldValue(updateRequest.oldValue())
                        .newValue(updateRequest.newValue())
                        .columnName(updateRequest.columnName())
                        .userId(userr.getId())
                        .build();
        historyOfChangesList.add(historyOfChanges);

        exactColumns.setHistoryOfChanges(historyOfChangesList);

        exactColumnsRepository.save(exactColumns);

    }

    public void requestForChangingFixedValue(RequestForFixedValueModel model, String userId) {

        RequestForFixedValue requestForFixedValue =
                RequestForFixedValue.builder()
                        .oldValue(model.oldValue())
                        .newValue(model.newValue())
                        .message(model.message())
                        .requestType(model.requestType())
                        .adminId(userId)
                        .columnName(model.columnName())
                        .status(RequestStatus.WAITING)
                        .build();

        changingValueRepository.save(requestForFixedValue);

    }
}
