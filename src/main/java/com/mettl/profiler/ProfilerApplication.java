package com.mettl.profiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProfilerApplication {
    public static void main(String[] args) {
        Logger log = LogManager.getRootLogger();

        log.debug("Starting Application");

        SpringApplication.run(ProfilerApplication.class, args);
	}
}
