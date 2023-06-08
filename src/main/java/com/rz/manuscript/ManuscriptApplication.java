package com.rz.manuscript;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableNeo4jRepositories
public class ManuscriptApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManuscriptApplication.class, args);
    }

}
