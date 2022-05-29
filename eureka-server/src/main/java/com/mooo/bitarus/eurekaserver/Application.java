package com.mooo.bitarus.eurekaserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.boot.SpringApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableEurekaServer
public class Application {
    private static Logger logger;

    public static void main(String[] args) {
        logger = LoggerFactory.getLogger(Application.class);
        logger.info("Starting application eurekaserver, service discovery (locator and registry)");
        SpringApplication.run(Application.class, args);
    }
}