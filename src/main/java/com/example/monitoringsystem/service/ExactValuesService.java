package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.AllColumns;
import com.example.monitoringsystem.model.NewColumnModel;
import com.example.monitoringsystem.model.UpdateRequest;
import com.example.monitoringsystem.model.WholeDepartment;
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

    public void saveData(AllColumns allColumns) {

        Department department =
                departmentRepository.getReferenceById(allColumns.getDepartmentId());

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

    public void saveDailyData(AllColumns allColumns) {

        Department department =
                departmentRepository.getReferenceById(allColumns.getDepartmentId());

        for (NewColumnModel model : allColumns.getNewColumns()) {
            //Firstly, I check if I have a column with this name
            if(newColumnRepository.findByName(model.getColumnName()).isPresent()){
                //Then, I find out and initiate it to its class
                NewColumn newColumn = newColumnRepository.findByName
                        (model.getColumnName()).orElseThrow(()
                        -> new BadRequestException("Error in getting column"));
                //Get all values of column which name is in given class
                List<String> values = newColumn.getValues();
                //Add to table new value to new/old column
                values.add(model.getValue());
                //Change values of column to old list + new value
                newColumn.setValues(values);
                //Save column with new value
                newColumnRepository.save(newColumn);
            }
            else {
                NewColumn newColumn = NewColumn.builder()
                        .values(List.of(model.getValue()))
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
    public WholeDepartment<ExactValues, List<NewColumnsToExactValue>> getFixedValues(Long departmentId) {
        ExactValues exactValues =
                exactValuesRepository.findByDepartmentId(departmentId);
        List<NewColumnsToExactValue> newColumnsToExactValueList =
                newColumnToExactValuesRepository.findByDepartmentId(departmentId);
        return new WholeDepartment<>(exactValues, newColumnsToExactValueList);
    }
    @Transactional
    public void updateColumns(UpdateRequest updateRequest, Long departmentId, String currentUserId){
        Long userId = Long.valueOf(currentUserId);
        ExactColumnsDTO exactColumnsDTO =
                updateRequest.getExactColumns();
        if(columnNamesRepository.existsByColumnName(updateRequest.getColumnName())) {
            ExactColumns exactColumns =
                    exactColumnsRepository.getReferenceById(exactColumnsDTO.getId());
            exactColumnsMapper.updateChangedColumn(exactColumnsDTO, exactColumns);
            exactColumnsRepository.save(exactColumns);
        }
        else{
            NewColumn newColumn =
                    newColumnRepository.findByNameAndDepartment_Id(updateRequest.getColumnName(), departmentId);
            newColumn.setValue(updateRequest.getNewValue());
            newColumnRepository.save(newColumn);
        }
        Department department =
                departmentRepository.findByDepartmentId(departmentId);
        Userr userr =
                userRepository.findById(userId).
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

    }
