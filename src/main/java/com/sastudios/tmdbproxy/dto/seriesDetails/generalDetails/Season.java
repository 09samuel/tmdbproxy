package com.sastudios.tmdbproxy.dto.seriesDetails.generalDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Season {
    @JsonProperty("air_date")
    private String airDate;
    @JsonProperty("episode_count")
    private Integer episodeCount;
    private Integer id;
    private String name;
    private String overview;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("season_number")
    private Integer seasonNumber;
    @JsonProperty("vote_average")
    private Double voteAverage;
}