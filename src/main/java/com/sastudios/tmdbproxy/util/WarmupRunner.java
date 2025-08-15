package com.sastudios.tmdbproxy.util;

import com.sastudios.tmdbproxy.controller.SeriesController;
import com.sastudios.tmdbproxy.service.SeriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class WarmupRunner implements ApplicationRunner {
    private final SeriesService service;

    private static final Logger log = LoggerFactory.getLogger(WarmupRunner.class);

    public WarmupRunner(SeriesService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Trigger a small TMDB call in the background
        service.getSeriesList(1)
                .doOnNext(list -> log.info("Warm-up complete: {} series fetched", list.getResults().size()))
                .doOnError(e -> log.error("Warm-up failed", e))
                .subscribe();
    }
}
