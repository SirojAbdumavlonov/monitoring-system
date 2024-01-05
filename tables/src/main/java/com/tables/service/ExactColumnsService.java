package com.tables.service;


import com.tables.entity.*;
import com.tables.model.*;
import com.util.constants.Localhost;
import com.util.exception.BadRequestException;
import com.tables.constants.TimeRange;
import com.tables.repository.EfficiencyRepository;
import com.tables.repository.ExactColumnsRepository;
import com.tables.repository.ExactValuesRepository;
import com.util.model.ChangedColumn;
import com.util.model.DepartmentAndSubBranches;
import com.util.model.NewColumnModel;
import com.util.tool.DateUtil;
import com.tables.tool.MapperProperties;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.util.model.ExactColumnsDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExactColumnsService {
    private final ExactColumnsRepository exactColumnsRepository;
    private final EfficiencyRepository efficiencyRepository;
    private final ExactValuesRepository exactValuesRepository;
    private final MapperProperties mapperProperties;
    private final WebClient webClient;

    public List<ValueWithEfficiency> getPreviousDaysData(String userId, LocalDate date, String chosenDepartment,
                                                         LocalDate from, LocalDate to, String timeRange,
                                                         String monthName, Integer lastNDays, Collection<? extends GrantedAuthority> role) {

        //Given date or search through or in
        // diaposon of date should be assigned
        // to variable time range

        FromAndToDates dates =
                getFromAndToDates(date, from, to, timeRange, monthName, lastNDays);

        from = dates.from();
        to = dates.to();


        return getPreviousDaysData(userId, chosenDepartment, from, to, role);
    }
    public List<ValueWithEfficiency> getPreviousDaysData(String userId, String chosenDepartment,
                                                         LocalDate from, LocalDate to,
                                                         Collection<? extends GrantedAuthority> role){

        //1. Find department id where he/she works - done
        //2. Use department id to find table(exactColumns) in which data is saved
        //3. Write a query to find this data(use IN keyword in sql)
        //4. Add it for method in service and ,eventually, to controller

        DepartmentAndSubBranches department = webClient.get()
                .uri(Localhost.DEPT + "dept-of-user/" + userId)
                .retrieve()
                .bodyToMono(DepartmentAndSubBranches.class)
                .block();

        String departmentId = department.departmentId();

        List<ExactColumns> exactColumnsList = null;
        List<Efficiency> efficiencyList = null;

        Boolean existenceByIdAndSubBranches =
                webClient.get()
                        .uri(Localhost.DEPT + "check-dept-subbranches/" + departmentId+ "-" + chosenDepartment)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();

        if (role.stream().anyMatch(a -> a.getAuthority().equals("USER"))) {

            if (Boolean.TRUE.equals(existenceByIdAndSubBranches)) {

                if (chosenDepartment == null) { //of all departments of user

                    List<String> subBranchesIds =
                            department.subBranchesId();

                    subBranchesIds.add(departmentId);

                    exactColumnsList = exactColumnsRepository.findAllByDepartmentIdInAndDateBetweenOrderByDateDescDepartmentId
                            (subBranchesIds, from, to);
                    efficiencyList = efficiencyRepository.findAllByDepartmentIdInAndDateBetweenOrderByDateDescDepartmentId
                            (subBranchesIds, from, to);
                    return mergeValueWithEfficiency(efficiencyList, exactColumnsList);
                    //sub-branches = branches belong to one of main branches
                }
            }
        }
        else{ //This is for super admin
            if(chosenDepartment == null){//if department is     not chosen, show all depts
                exactColumnsList = exactColumnsRepository
                        .findAllByDateBetweenOrderByDateDescDepartmentId(from, to);
                efficiencyList = efficiencyRepository
                        .findAllByDateBetweenOrderByDateDescDepartmentId(from, to);
                return mergeValueWithEfficiency(efficiencyList, exactColumnsList);
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

            exactColumnsList = exactColumnsRepository.findAllByDepartmentIdAndDateBetweenOrderByDateDesc
                    (chosenDepartment, from, to);

            efficiencyList = efficiencyRepository.findAllByDepartmentIdAndDateBetweenOrderByDateDesc
                    (chosenDepartment, from, to);

            return mergeValueWithEfficiency(efficiencyList, exactColumnsList);
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
            listList.add(valueWithEfficiency.values().getHistoryOfChanges());
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

    public List<ValueWithEfficiency> mergeValueWithEfficiency
            (List<Efficiency> efficiencyList, List<ExactColumns> exactColumnsList){

        List<ValueWithEfficiency> valueWithEfficiencies = null;

        for(int i = 0; i < efficiencyList.size(); i++){
            valueWithEfficiencies.add(
                    new ValueWithEfficiency(exactColumnsList.get(i), efficiencyList.get(i))
            );
        }

        return valueWithEfficiencies;
    }

    //In this method I check if this department can be seen by this user
    public boolean ifThisDepartmentCanBeSeenByThisUser(String checkedDeptId, String userId){

        return Boolean.TRUE.equals(webClient.get()
                .uri(Localhost.DEPT + "check-dept-access/" + checkedDeptId + "-" + userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());
    }
    public FromAndToDates getFromAndToDates(LocalDate date, LocalDate from,
                                            LocalDate to, String timeRange,
                                            String monthName, Integer lastNDays){
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
    @Transactional
    public Object saveDailyData(AllColumns allColumns, String departmentId) {

        List<NewColumn> columns = null;

        ZoneId uzbekistanTimeZone = ZoneId.of("Asia/Tashkent");

        Boolean existsDepartment = webClient.get()
                .uri(Localhost.DEPT + "check-dept/" + departmentId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        LocalDate today = LocalDate.now(uzbekistanTimeZone);

        if(Boolean.FALSE.equals(existsDepartment)){
            throw new BadRequestException("Department not found!");
        }
        if(exactColumnsRepository.existsByDate(today)){
            throw new BadRequestException("You have already saved for this date! \n You can update only!");
        }

        ExactValues exactValues =
                exactValuesRepository.findByDepartmentId(departmentId);

        List<NewColumnEfficiency> efficiencyList = null;

        double newColumnsEfficiency = 0.0;
        if (allColumns.newColumns() != null) {
            columns = new ArrayList<>();
            for (NewColumnModel model : allColumns.newColumns()) {
                columns.add(
                        NewColumn.builder()
                                .name(model.columnName())
                                .value(model.value())
                                .build()
                );
            }
            List<NewColumnsToExactValue> exactValuesList =
                    exactValues.getNewColumnsToExactValueList();

            double helper;
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
                    }
                }
            }
        }

        //Find all fixed values of chosen department

        Efficiency efficiency =
                Efficiency.builder()
                        .bankomats(getEfficiency(allColumns.bankomats(), exactValues.getBankomats()))
                        .monitor(getEfficiency(allColumns.monitor(), exactValues.getMonitor()))
                        .mouse(getEfficiency(allColumns.mouse(), exactValues.getMouse()))
                        .computers(getEfficiency(allColumns.computers(), exactValues.getComputers()))
                        .printer(getEfficiency(allColumns.printer(), exactValues.getPrinter()))
                        .employees(getEfficiency(allColumns.employees(), exactValues.getEmployees()))
                        .keyboard(getEfficiency(allColumns.keyboard(), exactValues.getKeyboard()))
                        .efficiencyList(efficiencyList)
                        .totalEfficiency(getTotalEfficiency(allColumns, exactValues, newColumnsEfficiency))
                        .departmentId(departmentId)
                        .date(today)
                        .build();

        ExactColumns exactColumns =
                ExactColumns.builder()
                        .bankomats(allColumns.bankomats())
                        .computers(allColumns.computers())
                        .mouse(allColumns.mouse())
                        .monitor(allColumns.monitor())
                        .printer(allColumns.printer())
                        .keyboard(allColumns.keyboard())
                        .newColumns(columns)
                        .date(today)
                        .departmentId(departmentId)
                        .employees(allColumns.employees())
                        .build();

        efficiencyRepository.save(efficiency);

        exactColumnsRepository.save(exactColumns);

        return new WholeDepartment<>(exactColumns, efficiency);
    }
    private Double getEfficiency(int dailyData, int fixedData){
        System.out.println("daily data = " + dailyData + ", fixed data = " + fixedData);
        return ( (double) dailyData / (double) fixedData) * 100;
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

    @Transactional
    public void updateColumns(UpdateRequest updateRequest, String departmentId, String currentUserId){

        Boolean existsDepartment = webClient.get()
                .uri(Localhost.DEPT + "check-dept/" + departmentId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(Boolean.FALSE.equals(existsDepartment)){
            throw new BadRequestException("Department not found!");
        }

        log.info("Date = {}, department id = {}",updateRequest.date(), departmentId);
        //todo: error should be corrected which is in finding row
        ExactColumns exactColumns =
                exactColumnsRepository.findByDateAndDepartmentId(updateRequest.date(), departmentId)
                        .orElseThrow(() -> new BadRequestException("Error in finding table of department!"));

        ExactColumnsDTO exactColumnsDTO =
                updateRequest.exactColumns();//get all changed columns

        HashMap columnExists = (webClient.get()
                .uri(Localhost.REQUEST + "check-column-names-changed-column")
                .retrieve()
                .bodyToMono(HashMap.class)
                .block());
        for(ChangedColumn changedColumn: updateRequest.changedColumns()) {

            //todo: change to normal one

            if (columnExists.containsKey(changedColumn.columnName())) {//check if column was updated from main table
                //if it is in main table, change the value
                log.info("dto = {}", exactColumnsDTO);
                mapperProperties.copyNonNullProperties(exactColumnsDTO, exactColumns);
                //change the value
                log.info("new obj = {}", exactColumns);
            } else {
                if (exactColumns.getNewColumns() != null) {
                    for (NewColumn newColumn : exactColumns.getNewColumns()) {
                        if (newColumn.getName().equals(changedColumn.columnName())) {
                            newColumn.setValue(changedColumn.newValue());
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
        }
        List<HistoryOfChanges> historyOfChangesList;

        String foundId = webClient.get()
                .uri(Localhost.AUTH + "user-id/" + currentUserId)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if(exactColumns.getHistoryOfChanges() == null){
            historyOfChangesList = new ArrayList<>();
        }else {
            historyOfChangesList = exactColumns.getHistoryOfChanges();
        }
        log.info("Changes started");
        for(ChangedColumn changedColumn: updateRequest.changedColumns()) {
            log.info("update request {}", updateRequest.changedColumns());
            HistoryOfChanges historyOfChanges =
                    HistoryOfChanges.builder()
                            .oldValue(changedColumn.oldValue())
                            .newValue(changedColumn.newValue())
                            .columnName(changedColumn.columnName())
                            .userId(foundId)
                            .createdDateTime(LocalDateTime.now())
                            .build();
            historyOfChangesList.add(historyOfChanges);
        }
        exactColumns.setHistoryOfChanges(historyOfChangesList);

        exactColumnsRepository.save(exactColumns);

    }



}
