package com.rz.manuscript;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ManuscriptApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManuscriptApplication.class, args);
    }

}
