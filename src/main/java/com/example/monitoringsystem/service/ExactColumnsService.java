package com.example.monitoringsystem.service;

import com.example.monitoringsystem.constants.Tab;
import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.entity.NewColumn;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.payload.ColumnNames;
import com.example.monitoringsystem.repository.ColumnNamesRepository;
import com.example.monitoringsystem.repository.DepartmentRepository;
import com.example.monitoringsystem.repository.ExactColumnsRepository;
import com.example.monitoringsystem.tool.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.monitoringsystem.constants.TimeRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExactColumnsService {
    private final ExactColumnsRepository exactColumnsRepository;
    private final DepartmentService departmentService;
    private final ColumnNamesRepository columnNamesRepository;
    private final DepartmentRepository departmentRepository;


    public Object getPreviousDaysData(String userId, String date, String view,
                                      LocalDate from, LocalDate to, String timeRange, String monthName) {
        LocalDate localDate = null;

        if(!date.equals(Tab.ALL_DATES)){
            localDate = LocalDate.parse(date);
        }

        switch (timeRange.toLowerCase()) {
            case TimeRange.TIME_RANGE:

                break;
            case TimeRange.MONTH:
                 // You can change this value as needed
                int monthNUmber = DateUtil.getMonthNumber(monthName);
                from = DateUtil.getStartOfMonth(LocalDate.now().getYear(), monthNUmber);
                to = DateUtil.getEndOfMonth(LocalDate.now().getYear(), monthNUmber);
                break;
            case TimeRange.LAST_WEEK:
                from = DateUtil.getStartOfLastWeek();
                to = DateUtil.getEndOfLastWeek();
                break;
            case TimeRange.THIS_MONTH:
                from = DateUtil.getStartOfCurrentMonth();
                to = DateUtil.getEndOfCurrentMonth();
                break;
            case TimeRange.LAST_MONTH:
                from = DateUtil.getStartOfLastMonth();
                to = DateUtil.getEndOfLastMonth();
                break;
            case TimeRange.THIS_YEAR:
                from = DateUtil.getStartOfCurrentYear();
                to = DateUtil.getEndOfCurrentYear();
                break;
            case TimeRange.LAST_YEAR:
                from = DateUtil.getStartOfLastYear();
                to = DateUtil.getEndOfLastYear();
                break;
            default:
                from = DateUtil.getStartOfCurrentWeek();
                to = DateUtil.getEndOfCurrentWeek();
                break;
                //default, it will show current week's data
        }


        //view is departmentId

        //1. Find department id where he/she works - done
        //2. Use department id to find table(exactColumns) in which data is saved
        //3. Write a query to find this data(use IN keyword in sql)
        //4. Add it for method in service and ,eventually, to controller
        String departmentId = departmentService.findDepartmentOfUser(userId).getId();
        
        List<String> subBranchesIds = departmentRepository.findAllByIdOfMainBranch(departmentId);
        subBranchesIds.add(departmentId);

        if(departmentRepository.existsById(departmentId) || departmentRepository.existsByIdOfMainBranch(view)) {

            if (date.equals(Tab.ALL_DATES)) {
                //find all dates
                if(view == null){//of all departments of user
                    return exactColumnsRepository.findAllByDepartmentIdIn(subBranchesIds);
                    //sub-branches = branches belong to one of main branches
                }
                else{
                    return exactColumnsRepository.findAllByDepartmentIdAndCreatedDateBetween
                            (view, from, to);
                    //view is given department id, which I want to find
                }
            }

            else if(!date.equals(Tab.ALL_DATES) && !view.isEmpty()){
                return exactColumnsRepository.findByCreatedDateAndDepartmentId(localDate, view);
            }

            return exactColumnsRepository.findAllByDepartmentId(departmentId);
        }
    }
    public ColumnNames getNamesOfColumns(String userId){

        String departmentId = departmentService.findDepartmentOfUser(userId).getId();

        List<String> namesOfColumnOfDefaultTable =
                columnNamesRepository.findAllColumnNames();

        ExactColumns exactColumns =
                exactColumnsRepository.findByDepartmentId(departmentId)
                        .orElseThrow(() -> new BadRequestException("Columns not found!"));
        if(!exactColumns.getNewColumns().isEmpty()) {
            List<String> namesOfNewColumns = new ArrayList<>();
            for (NewColumn newColumn : exactColumns.getNewColumns()) {
                namesOfNewColumns.add(
                        newColumn.getName()
                );
            }
            namesOfColumnOfDefaultTable.addAll(namesOfNewColumns);
        }
        return new ColumnNames(namesOfColumnOfDefaultTable);
    }
    public ExactColumns getTodayDailyFilledData(LocalDate today, String userId){

        String departmentId = departmentService.findDepartmentOfUser(userId).getId();

        return exactColumnsRepository.findByCreatedDateAndDepartmentId(today, departmentId)
                .orElseThrow(() -> new BadRequestException("Not found table with data!"));
    }

}
