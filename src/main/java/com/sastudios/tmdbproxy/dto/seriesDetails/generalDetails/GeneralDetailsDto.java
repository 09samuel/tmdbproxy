package com.sastudios.tmdbproxy.dto.seriesDetails.generalDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class GeneralDetailsDto {
    private Boolean adult;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("created_by")
    private List<CreatedBy> createdBy;
    @JsonProperty("episode_run_time")
    private List<Object> episodeRunTime;
    @JsonProperty("first_air_date")
    private String firstAirDate;
    private List<Genre> genres;
    private String homepage;
    private Integer id;
    @JsonProperty("in_production")
    private Boolean inProduction;
    private List<String> languages;
    @JsonProperty("last_air_date")
    private String lastAirDate;
    private String name;
    private List<Network> networks;
    @JsonProperty("number_of_episodes")
    private Integer numberOfEpisodes;
    @JsonProperty("number_of_seasons")
    private Integer numberOfSeasons;
    @JsonProperty("origin_country")
    private List<String> originCountry;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("original_name")
    private String originalName;
    private String overview;
    private Double popularity;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies;
    @JsonProperty("production_countries")
    private List<ProductionCountry> productionCountries;
    private List<Season> seasons;
    @JsonProperty("spoken_languages")
    private List<SpokenLanguage> spokenLanguages;
    private String status;
    private String tagline;
    private String type;
    @JsonProperty("vote_average")
    private Double voteAverage;
    @JsonProperty("vote_count")
    private Integer voteCount;
}

