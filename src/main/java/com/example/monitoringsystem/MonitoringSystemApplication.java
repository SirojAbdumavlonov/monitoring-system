package com.example.monitoringsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitoringSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringSystemApplication.class, args);
    }

}
