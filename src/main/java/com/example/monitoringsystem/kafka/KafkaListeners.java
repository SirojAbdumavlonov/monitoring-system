//package com.example.monitoringsystem.kafka;
//
//import com.example.monitoringsystem.config.WebSocketClientExample;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@Service
//@RequiredArgsConstructor
//public class KafkaListeners {
//
//    private final SimpMessagingTemplate messagingTemplate;
//    private final WebSocketClientExample webSocketClientExample;
//
//    @KafkaListener(
//            topics = "monitoring-system",
//            groupId = "groupId"
//    )
//    void listener(String data){
//        System.out.println("Listener received: " + data);
////        messagingTemplate.convertAndSend("/topic/super-admin", data);
//        webSocketClientExample.connectToWebSocket();
//    }
//
//}
