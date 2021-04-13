package com.autopay.autopay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutoPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoPayApplication.class, args);
    }

}
