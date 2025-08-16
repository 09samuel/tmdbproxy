package com.sastudios.tmdbproxy.service;

import com.sastudios.tmdbproxy.dto.seriesDetails.episodes.EpisodesListDto;
import com.sastudios.tmdbproxy.dto.seriesDetails.generalDetails.GeneralDetailsDto;
import com.sastudios.tmdbproxy.dto.seriesDetails.recommendations.RecommendationsListDto;
import com.sastudios.tmdbproxy.dto.seriesDetails.watchProvider.SeriesWatchProvidersDto;
import com.sastudios.tmdbproxy.dto.seriesList.SeriesListDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class SeriesService {

    private static final Logger log = LoggerFactory.getLogger(SeriesService.class);

    private final WebClient client;
    private final String apiKey;

    public SeriesService(WebClient tmdbWebClient,
                         @Value("${tmdb.api-key}") String apiKey) {
        this.client = tmdbWebClient;
        this.apiKey = apiKey;
    }

    //    @Cacheable("topRated")
    @Cacheable(value = "topRated", key = "#page", unless = "#result == null")
    @Retry(name = "tmdb")
    @CircuitBreaker(name = "tmdb", fallbackMethod = "fallbackSeriesList")
    public Mono<SeriesListDto> getSeriesList(int page) {
        log.info("[CACHE MISS] topRated page {} → Fetching from TMDB API", page);
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tv/top_rated")
                        .queryParam("page", page)
                        .queryParam("language", "en-US")
                        .queryParam("api_key", apiKey)
                        .build())
                .headers(headers -> {
                    log.info("Proxying to TMDB with headers: {}", headers);
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    log.error("TMDB returned error status: {}", response.statusCode());
                    return response.createException();
                })
                .bodyToMono(SeriesListDto.class);
    }

        public Mono<SeriesListDto> searchSeries(String query, int page) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/tv")
                        .queryParam("query", query)
                        .queryParam("include_adult", false)
                        .queryParam("language", "en-US")
                        .queryParam("page", page)
                        .queryParam("api_key", apiKey)
                        .build())
                .headers(headers -> {
                    log.info("Proxying to TMDB with headers: {}", headers);
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    log.error("TMDB returned error status: {}", response.statusCode());
                    return response.createException();
                })
                .bodyToMono(SeriesListDto.class);
    }

    public Mono<GeneralDetailsDto> getGeneralDetails(int seriesId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tv/{id}")
                        .queryParam("language", "en-US")
                        .queryParam("api_key", apiKey)
                        .build(seriesId))
                .retrieve()
                .bodyToMono(GeneralDetailsDto.class);
    }

    public Mono<RecommendationsListDto> getRecommendations(int seriesId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tv/{id}/recommendations")
                        .queryParam("language", "en-US")
                        .queryParam("api_key", apiKey)
                        .build(seriesId))
                .headers(headers -> {
                    log.info("Proxying to TMDB with headers: {}", headers);
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    log.error("TMDB returned error status: {}", response.statusCode());
                    return response.createException();
                })
                .bodyToMono(RecommendationsListDto.class);
    }

    public Mono<EpisodesListDto> getEpisodes(int seriesId, int seasonNo) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tv/{id}/season/{season}")
                        .queryParam("language", "en-US")
                        .queryParam("api_key", apiKey)
                        .build(seriesId, seasonNo))
                .retrieve()
                .bodyToMono(EpisodesListDto.class);
    }

    public Mono<SeriesWatchProvidersDto> getWatchProviders(int seriesId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tv/{id}/season/1/watch/providers")
                        .queryParam("language", "en-US")
                        .queryParam("api_key", apiKey)
                        .build(seriesId))
                .headers(headers -> {
                    log.info("Proxying to TMDB with headers: {}", headers);
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    log.error("TMDB returned error status: {}", response.statusCode());
                    return response.createException();
                })
                .bodyToMono(SeriesWatchProvidersDto.class);
    }

    // Fallback
    private Mono<SeriesListDto> fallbackSeriesList(int page, Throwable t) {
        log.error("Fallback triggered for topRated page {} due to {}", page, t.toString());
        SeriesListDto fallback = new SeriesListDto(page, Collections.emptyList(), 0, 0);
        fallback.setPage(page);
        fallback.setResults(List.of());
        return Mono.just(fallback);
    }
}
