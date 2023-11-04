package com.example.monitoringsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private final ExactColumnsService exactColumnsService;
    private final ExactValuesService exactValuesService;
    //This class is created to check every day at 00:00 daily data fillings,
    //while checking if it was created yesterday of that day and create reports
    @Scheduled(cron = "1 0 0 * * ?")
    public void checkAndAddReport(){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        //Finished here, I should compare all values and store it to new
        //table called efficiency by data and department id
        //all values I can take using exact values service

    }

}
