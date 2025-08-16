package com.sastudios.tmdbproxy.dto.seriesDetails.episodes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodesListDto {
    @JsonProperty("_id")
    private String _id;

    @JsonProperty("air_date")
    private String airDate;

    private List<EpisodeDto> episodes;
    private Integer id;
    private String name;

    // Optional season details
    // private String overview;
    // @JsonProperty("poster_path")
    // private String posterPath;
    // @JsonProperty("season_number")
    // private Integer seasonNumber;
    // @JsonProperty("vote_average")
    // private Double voteAverage;
}
