package com.sastudios.tmdbproxy.dto.seriesDetails.episodes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EpisodeDto {
    private Integer id;

    @JsonProperty("season_number")
    private Integer seasonNumber;

    @JsonProperty("episode_number")
    private Integer episodeNumber;

    private String name;

    @JsonProperty("still_path")
    private String stillPath;

    @JsonProperty("air_date")
    private String airDate;

    @JsonProperty("episode_type")
    private String episodeType;

    private String overview;

    @JsonProperty("production_code")
    private String productionCode;

    private Integer runtime;

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("vote_count")
    private Integer voteCount;

    // Optional
    // @JsonProperty("show_id")
    // private Integer showId;
    // private List<CrewDto> crew;
    // @JsonProperty("guest_stars")
    // private List<GuestStarDto> guestStars;
}
