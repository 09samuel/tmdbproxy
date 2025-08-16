package com.sastudios.tmdbproxy.controller;

import com.sastudios.tmdbproxy.dto.seriesDetails.episodes.EpisodesListDto;
import com.sastudios.tmdbproxy.dto.seriesDetails.generalDetails.GeneralDetailsDto;
import com.sastudios.tmdbproxy.dto.seriesDetails.recommendations.RecommendationsListDto;
import com.sastudios.tmdbproxy.dto.seriesDetails.watchProvider.SeriesWatchProvidersDto;
import com.sastudios.tmdbproxy.dto.seriesList.SeriesListDto;
import com.sastudios.tmdbproxy.service.SeriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/tv")
@Tag(name = "TV Series API", description = "Endpoints for TV series data")
public class SeriesController {

    private static final Logger log = LoggerFactory.getLogger(SeriesController.class);
    private final SeriesService service;

    public SeriesController(SeriesService service) {
        this.service = service;
    }

    @Operation(summary = "Get top-rated series", description = "Returns paginated list of top-rated TV series")
    @GetMapping("/top-rated")
    public Mono<SeriesListDto> topRated(@RequestParam(defaultValue = "1") @Min(1) int page) {
        log.info("Fetching top-rated series, page={}", page);
        return service.getSeriesList(page)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> {
                    if (e instanceof TimeoutException || e instanceof IOException) {
                        log.warn("TMDB unavailable, returning empty fallback", e);
                        return Mono.just(new SeriesListDto(page, Collections.emptyList(), 0, 0));
                    }
                    return Mono.error(e); // Let GlobalExceptionHandler handle
                });
    }


    @GetMapping("/search")
    public Mono<SeriesListDto> search(@RequestParam String query,
                                      @RequestParam(defaultValue = "1") @Min(1) int page) {
        log.info("Searching series with query='{}', page={}", query, page);
        return service.searchSeries(query, page)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> {
                    if (e instanceof TimeoutException || e instanceof IOException) {
                        log.warn("Search API unavailable, returning empty fallback", e);
                        return Mono.just(new SeriesListDto(page, Collections.emptyList(), 0, 0));
                    }
                    return Mono.error(e);
                });
    }

    @Operation(summary = "Get series general details")
    @GetMapping("/{seriesId}")
    public Mono<GeneralDetailsDto> details(@PathVariable @Min(1) int seriesId) {
        return service.getGeneralDetails(seriesId)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> {
                    if (e instanceof TimeoutException || e instanceof IOException) {
                        log.warn("General details API unavailable, returning empty fallback", e);
                        return Mono.empty();
                    }
                    return Mono.error(e);
                });
    }

    @Operation(summary = "Get series recommendations")
    @GetMapping("/{seriesId}/recommendations")
    public Mono<RecommendationsListDto> recommendations(@PathVariable @Min(1) int seriesId) {
        return service.getRecommendations(seriesId)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> {
                    if (e instanceof TimeoutException || e instanceof IOException) {
                        log.warn("Recommendations API unavailable, returning empty fallback", e);
                        return Mono.just(new RecommendationsListDto(1, Collections.emptyList(), 0, 0));
                    }
                    return Mono.error(e);
                });
    }

    @Operation(summary = "Get episodes for a season")
    @GetMapping("/{seriesId}/season/{seasonNo}/episodes")
    public Mono<EpisodesListDto> episodes(@PathVariable @Min(1) int seriesId,
                                          @PathVariable @Min(1) int seasonNo) {
        return service.getEpisodes(seriesId, seasonNo)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> {
                    if (e instanceof TimeoutException || e instanceof IOException) {
                        log.warn("Episodes API unavailable, returning empty fallback", e);
                        return Mono.just(new EpisodesListDto(
                                null,        // _id
                                null,        // airDate
                                Collections.emptyList(), // episodes
                                null,        // id
                                null         // name
                        ));

                    }
                    return Mono.error(e);
                });
    }

    @Operation(summary = "Get watch providers")
    @GetMapping("/{seriesId}/watch-providers")
    public Mono<SeriesWatchProvidersDto> watchProviders(@PathVariable @Min(1) int seriesId) {
        return service.getWatchProviders(seriesId)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> {
                    if (e instanceof TimeoutException || e instanceof IOException) {
                        log.warn("Watch providers API unavailable, returning empty fallback", e);
                        return Mono.just(new SeriesWatchProvidersDto(seriesId, Collections.emptyMap()));
                    }
                    return Mono.error(e);
                });
    }
}

