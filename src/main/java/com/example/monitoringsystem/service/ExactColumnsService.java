package com.example.monitoringsystem.service;


import com.example.monitoringsystem.entity.*;
import com.example.monitoringsystem.exception.BadRequestException;
import com.example.monitoringsystem.model.FromAndToDates;
import com.example.monitoringsystem.model.ValueWithEfficiency;
import com.example.monitoringsystem.repository.DepartmentRepository;
import com.example.monitoringsystem.repository.EfficiencyRepository;
import com.example.monitoringsystem.repository.ExactColumnsRepository;
import com.example.monitoringsystem.tool.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import com.example.monitoringsystem.constants.TimeRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExactColumnsService {
    private final ExactColumnsRepository exactColumnsRepository;
    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;
    private final ReportService reportService;
    private final EfficiencyRepository efficiencyRepository;

    public List<ValueWithEfficiency> getPreviousDaysData(String userId, LocalDate date, String chosenDepartment,
                                      LocalDate from, LocalDate to, String timeRange,
                                      String monthName, int lastNDays, Collection<? extends GrantedAuthority> role) {

        //Given date or search through or in
        // diaposon of date should be assigned
        // to variable time range

        FromAndToDates dates =
                getFromAndToDates(date, from, to, timeRange, monthName, lastNDays);

        from = dates.getFrom();
        to = dates.getTo();


        return getPreviousDaysData(userId, chosenDepartment, from, to, role);
    }
    public List<ValueWithEfficiency> getPreviousDaysData(String userId,String chosenDepartment,
                                                         LocalDate from, LocalDate to,
                                                         Collection<? extends GrantedAuthority> role){

        //1. Find department id where he/she works - done
        //2. Use department id to find table(exactColumns) in which data is saved
        //3. Write a query to find this data(use IN keyword in sql)
        //4. Add it for method in service and ,eventually, to controller

        String departmentId = departmentService.findDepartmentOfUser(userId).getId();

        List<ExactColumns> exactColumnsList = null;
        List<Efficiency> efficiencyList = null;

        if (role.stream().anyMatch(a -> a.getAuthority().equals("USER"))) {

            if (departmentRepository.existsById(departmentId) || departmentRepository.existsByIdOfMainBranchAndId(departmentId, chosenDepartment)) {

                if (chosenDepartment == null) { //of all departments of user

                    List<String> subBranchesIds =
                            departmentRepository.findAllByIdOfMainBranch(departmentId);

                    subBranchesIds.add(departmentId);

                    exactColumnsList = exactColumnsRepository.findAllByDepartmentIdInAndCreatedDateBetweenOrderByCreatedDateDescDepartmentId
                            (subBranchesIds, from, to);
                    efficiencyList = efficiencyRepository.findAllByDepartmentIdInAndCreatedDateBetweenOrderByCreatedDateDescDepartmentId
                            (subBranchesIds, from, to);
                    return reportService.mergeValueWithEfficiency(efficiencyList, exactColumnsList);
                    //sub-branches = branches belong to one of main branches
                }
            }
        }
        else{ //This is for super admin
            if(chosenDepartment == null){//if department is     not chosen, show all depts
                exactColumnsList = exactColumnsRepository
                        .findAllByCreatedDateBetweenOrderByCreatedDateDescDepartmentId(from, to);
                efficiencyList = efficiencyRepository
                        .findAllByCreatedDateBetweenOrderByCreatedDateDescDepartmentId(from, to);
                return reportService.mergeValueWithEfficiency(efficiencyList, exactColumnsList);
            }

        }
        //this part for getting history of changes of chosen department

        if((ifThisDepartmentCanBeSeenByThisUser(chosenDepartment, userId) &&
                role.stream().anyMatch(a -> a.getAuthority().equals("USER")))
                ||
                role.stream().anyMatch(a -> a.getAuthority().equals("SUPER_ADMIN")
                )){
            //It will work if super admin is trying to access it, or
            //If this branch can be accessed by the admin of header branch

            exactColumnsList = exactColumnsRepository.findAllByDepartmentIdAndCreatedDateBetweenOrderByCreatedDateDesc
                    (chosenDepartment, from, to);

            efficiencyList = efficiencyRepository.findAllByDepartmentIdAndCreatedDateBetweenOrderByCreatedDateDesc
                    (chosenDepartment, from, to);

            return reportService.mergeValueWithEfficiency(efficiencyList, exactColumnsList);
            //view is given department id, which I want to find
        }
        throw new BadRequestException("Error in getting data!");
    }

    public Object getHistoryOfTableFilling(String userId, LocalDate date, String chosenDepartment,
                                    LocalDate from, LocalDate to, String timeRange,
                                    String monthName, int lastNDays, Collection<? extends GrantedAuthority> role){

        List<ValueWithEfficiency> valueWithEfficiencies = getPreviousDaysData
                (userId, date, chosenDepartment, from, to,
                        timeRange, monthName, lastNDays, role);

        List<List<HistoryOfChanges>> listList = new ArrayList<>();

        for (ValueWithEfficiency valueWithEfficiency : valueWithEfficiencies) {
            listList.add(valueWithEfficiency.getValues().getHistoryOfChanges());
        }
        return listList;
    }

//    public ColumnNames getNamesOfColumns(String userId){
//
//        String departmentId = departmentService.findDepartmentOfUser(userId).getId();
//
//        List<String> namesOfColumnOfDefaultTable =
//                columnNamesRepository.findAllColumnNames();
//
//        ExactColumns exactColumns =
//                exactColumnsRepository.findByDepartmentId(departmentId)
//                        .orElseThrow(() -> new BadRequestException("Columns not found!"));
//        if(!exactColumns.getNewColumns().isEmpty()) {
//            List<String> namesOfNewColumns = new ArrayList<>();
//            for (NewColumn newColumn : exactColumns.getNewColumns()) {
//                namesOfNewColumns.add(
//                        newColumn.getName()
//                );
//            }
//            namesOfColumnOfDefaultTable.addAll(namesOfNewColumns);
//        }
//        return new ColumnNames(namesOfColumnOfDefaultTable);
//    }

//    public ExactColumns getTodayDailyFilledData(LocalDate today, String userId){
//
//        String departmentId = departmentService.findDepartmentOfUser(userId).getId();
//
//        return exactColumnsRepository.findByCreatedDateAndDepartmentId(today, departmentId)
//                .orElseThrow(() -> new BadRequestException("Not found table with data!"));
//    }

    //In this method I check if this department can be seen by this user
    public boolean ifThisDepartmentCanBeSeenByThisUser(String checkedDeptId, String userId){

        departmentService.findDepartmentByIdForAdmin(checkedDeptId, userId);

        return true;
    }
    public FromAndToDates getFromAndToDates(LocalDate date, LocalDate from,
                                            LocalDate to, String timeRange,
                                            String monthName, int lastNDays){
        switch (timeRange.toLowerCase()) {
            case TimeRange.GIVEN_DATE:
                from = date;
                to = date;
                break;
            case TimeRange.LAST_N_DAYS:
                from = DateUtil.getStartOfLastNDays(lastNDays);
                to = LocalDate.now();
                break;
            case TimeRange.MONTH://if month is given then,
                // monthName should be given and time range should be month
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
            case TimeRange.ALL_TIME:
                from = LocalDate.of(2023, 11, 20);
                to = LocalDate.now();
                break;
            default:
                from = DateUtil.getStartOfCurrentWeek();
                to = DateUtil.getEndOfCurrentWeek();
                break;
            //default, it will show current week's data
        }
        return new FromAndToDates(from, to);
    }



}
