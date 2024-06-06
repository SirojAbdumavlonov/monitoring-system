//package com.example.monitoringsystem.kafka;
//
//import com.example.monitoringsystem.model.ApiResponse;
//import com.example.monitoringsystem.model.KafkaMessage;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@AllArgsConstructor
//public class KafkaProducer {
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @PostMapping("/send-data-kafka")
//    public ResponseEntity<?> sendDataKafka(@RequestBody KafkaMessage data){
//        kafkaTemplate.send("monitoring-system",data.message());
//        return ResponseEntity.ok(new ApiResponse("Sent successfully: " + data.message()));
//    }
//}
