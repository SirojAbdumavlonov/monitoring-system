package com.tables;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EntityScan("com.tables")
@EnableMongoRepositories("com.tables.repository")
public class TablesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TablesApplication.class, args);
    }

}
