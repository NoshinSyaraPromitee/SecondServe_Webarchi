package com.secondserve.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecondServeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecondServeServerApplication.class, args);
        System.out.println("SecondServe Server started successfully!");
    }
}