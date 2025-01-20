package com.mooo.bitarus.luceneworker;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ComponentScan
@EnableAutoConfiguration
public class Application {
    private static Logger logger;

    public static void main(String[] args) {
        logger = LoggerFactory.getLogger(Application.class);
        logger.info("Starting application luceneworker");
        SpringApplication.run(Application.class, args);
    }
}