package com.mooo.bitarus.chucknorris;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ComponentScan // scans for @Component beans
@EnableAutoConfiguration
@EnableEurekaClient
@EntityScan(basePackages = "com.mooo.bitarus.chucknorris")
public class Application {
    private static Logger logger;

    public static void main(String[] args) {
        logger = LoggerFactory.getLogger(Application.class);
        logger.info("Starting application chuck-norris.");
        SpringApplication.run(Application.class, args);
    }
}