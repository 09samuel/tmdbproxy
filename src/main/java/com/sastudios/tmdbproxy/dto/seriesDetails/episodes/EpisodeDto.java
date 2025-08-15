package com.sastudios.tmdbproxy.dto.seriesDetails.episodes;

import lombok.Data;

@Data
public class EpisodeDto {
    private Integer id;
    private Integer seasonNumber;
    private Integer episodeNumber;
    private String name;
    private String stillPath;
    private String airDate;
    private String episodeType;
    private String overview;
    private String productionCode;
    private Integer runtime;
    private Double voteAverage;
    private Integer voteCount;

    // Optional extra fields (uncomment if needed)
    // private Integer showId; // seriesId
    // private List<CrewDto> crew;
    // private List<GuestStarDto> guestStars;
}
