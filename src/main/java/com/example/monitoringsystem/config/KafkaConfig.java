//package com.example.monitoringsystem.config;
//
//import org.apache.kafka.clients.admin.AdminClientConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.KafkaAdmin;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaConfig {
//
//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        config.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000); // Set the timeout in milliseconds
//        return new KafkaAdmin(config);
//    }
//}