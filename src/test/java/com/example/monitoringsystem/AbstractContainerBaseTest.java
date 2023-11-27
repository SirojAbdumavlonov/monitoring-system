package com.example.monitoringsystem;

import org.testcontainers.containers.MongoDBContainer;

public class AbstractContainerBaseTest {
    static final MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new MongoDBContainer("mongo:latest");

        mongoDBContainer.start();
    }
}
