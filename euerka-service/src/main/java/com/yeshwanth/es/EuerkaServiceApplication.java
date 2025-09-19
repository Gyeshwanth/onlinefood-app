package com.yeshwanth.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EuerkaServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EuerkaServiceApplication.class, args);
    }

}
