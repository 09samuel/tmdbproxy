package com.sastudios.tmdbproxy.dto.seriesList;

import lombok.Data;
import java.util.List;

@Data
public class SeriesDto {
    private Boolean adult;
    private String backdropPath;
    private String firstAirDate;
    private List<Integer> genreIds;
    private Integer id;
    private String name;
    private List<String> originCountry;
    private String originalLanguage;
    private String originalName;
    private String overview;
    private Double popularity;
    private String posterPath;
    private Double voteAverage;
    private Integer voteCount;
}