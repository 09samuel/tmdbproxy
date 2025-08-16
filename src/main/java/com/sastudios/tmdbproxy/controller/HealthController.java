package com.sastudios.tmdbproxy.controller;

import com.sastudios.tmdbproxy.config.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/ping")
    public String ping() {
        log.info("ping");
        return "OK";
    }
}
