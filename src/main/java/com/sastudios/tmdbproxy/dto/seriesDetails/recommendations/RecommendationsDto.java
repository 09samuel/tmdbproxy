package com.sastudios.tmdbproxy.dto.seriesDetails.recommendations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RecommendationsDto {
    private Integer id;

    @JsonProperty("poster_path")
    private String posterPath;

    private String name;
}
