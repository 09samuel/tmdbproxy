package com.sastudios.tmdbproxy.dto.seriesDetails.watchProvider;

import lombok.Data;

import java.util.List;

@Data
public class CountryWatchProvider {
    private String link;
    private List<Provider> flatrate;
    private List<Provider> buy;
}