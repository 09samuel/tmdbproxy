package com.sastudios.tmdbproxy.dto.seriesDetails.watchProvider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeriesWatchProvidersDto {
    private Integer id;
    private Map<String, CountryWatchProvider> results;


}