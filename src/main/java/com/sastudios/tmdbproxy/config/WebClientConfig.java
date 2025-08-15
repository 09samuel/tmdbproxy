package com.sastudios.tmdbproxy.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient tmdbWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 5s connect timeout
                .responseTimeout(Duration.ofSeconds(5)) // 5s read timeout
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS))
                )
                .keepAlive(true) // keep TCP connections alive
                .compress(true); // enable gzip

        return WebClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .filter(logRequest()) // logging for debugging
//                .filter(logResponse())
                .build();
    }

//    private ExchangeFilterFunction logRequest() {
//        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
//            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
//            return reactor.core.publisher.Mono.just(clientRequest);
//        });
//    }
//
//    private ExchangeFilterFunction logResponse() {
//        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
//            System.out.println("Response status: " + clientResponse.statusCode());
//            return reactor.core.publisher.Mono.just(clientResponse);
//        });
//    }
}
