package com.mooo.bitarus.springcloudgwserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Application {
    private static Logger logger;

    public static void main(String[] args) {
        logger = LoggerFactory.getLogger(Application.class);
        logger.info("Starting application springcloudgwserver, API GW and load balancer");
        SpringApplication.run(Application.class, args);
    }
}