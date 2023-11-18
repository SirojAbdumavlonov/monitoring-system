//package com.example.monitoringsystem.service;
//
//import com.example.monitoringsystem.entity.*;
//import com.example.monitoringsystem.repository.EfficiencyRepository;
//import com.example.monitoringsystem.repository.NewColumnEfficiencyRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class ScheduledTasks {
//    private final ExactValuesService exactValuesService;
//    private final EfficiencyRepository efficiencyRepository;
//    private final NewColumnEfficiencyRepository newColumnEfficiencyRepository;
//    //This class is created to check every day at 00:00 daily data fillings,
//    //while checking if it was created yesterday of that day and create reports
//    @Scheduled(cron = "1 0 0 * * ?")
//    public void checkAndAddReport(){
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = today.minusDays(1);
//        //Finished here, I should compare all values and store it to new
//        //table called efficiency by data and department id
//        //all values I can take using exact values service
//        //firstly, I should get fixed column with input column, and by creating
//        //efficiency object, saving data to it
//        double totalEfficiency = 0;
//        List<NewColumnsToExactValue> columnsToExactValues = null;
//        List<NewColumnEfficiency> newColumnEfficiencies = null;
//        ExactColumns exactColumns =
//                exactValuesService.getValues(yesterday).getColumns();
//
//        List<NewColumn> newColumnList =
//                exactValuesService.getValues(yesterday).getNewColumns();
//        if (newColumnList != null){
//            columnsToExactValues =
//                    exactValuesService.getFixedValues(exactColumns.
//                            getDepartment().getId()).getNewColumns();
//            for(int i = 0; i < columnsToExactValues.size(); i++) {
//                totalEfficiency +=
//                        getCalculatedEfficiency(columnsToExactValues.get(i).getValue(),
//                                newColumnList.get(i).getValues().getLast().getValue());
//            }
//        }
//
//        ExactValues exactValues =
//                exactValuesService.getFixedValues(exactColumns.
//                        getDepartment().getId()).getColumns();
//        Efficiency efficiency =
//                Efficiency.builder()
//                        .monitor(getCalculatedEfficiency(exactValues.getMonitor(), exactColumns.getMonitor()))
//                        .bankomats(getCalculatedEfficiency(exactValues.getBankomats(), exactColumns.getBankomats()))
//                        .mouse(getCalculatedEfficiency(exactValues.getMouse(), exactColumns.getMouse()))
//                        .computers(getCalculatedEfficiency(exactValues.getComputers(), exactColumns.getComputers()))
//                        .keyboard(getCalculatedEfficiency(exactValues.getKeyboard(), exactColumns.getKeyboard()))
//                        .employees(getCalculatedEfficiency(exactValues.getEmployees(), exactColumns.getEmployees()))
//                        .printer(getCalculatedEfficiency(exactValues.getPrinter(), exactColumns.getPrinter()))
//                        .totalEfficiency(totalEfficiency + getCalculatedEfficiency(exactValues.getMonitor(), exactColumns.getMonitor())
//                        + getCalculatedEfficiency(exactValues.getBankomats(), exactColumns.getBankomats()) + getCalculatedEfficiency(exactValues.getMouse(), exactColumns.getMouse())
//                        + getCalculatedEfficiency(exactValues.getComputers(), exactColumns.getComputers()) + getCalculatedEfficiency(exactValues.getKeyboard(), exactColumns.getKeyboard())
//                        + getCalculatedEfficiency(exactValues.getEmployees(), exactColumns.getEmployees()) + getCalculatedEfficiency(exactValues.getPrinter(), exactColumns.getPrinter()))
//                        .build();
//        efficiencyRepository.save(efficiency);
//
//        if (newColumnList != null) {
//
//            for(int i = 0; i < columnsToExactValues.size(); i++){
//                newColumnEfficiencies.add(
//                        newColumnEfficiencyRepository.save(
//                                NewColumnEfficiency.builder()
//                                .efficiency(efficiency)
//                                .value(getCalculatedEfficiency(columnsToExactValues.get(i).getValue(),newColumnList.get(i).getValues().getLast().getValue()))
//                                .name(columnsToExactValues.get(i).getName())
//                                .efficiency(efficiency)
//                                .build()
//                ));
//            }
//        }
//
//
//    }
//    private static Double getCalculatedEfficiency(int exact, int got){
//        return (double) ((got / exact) * 100);
//    }
//
//}
