package com.sastudios.tmdbproxy.dto.seriesDetails.episodes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GuestStarDto {
    private Boolean adult;
    private String character;

    @JsonProperty("credit_id")
    private String creditId;

    private Integer gender;
    private Integer id;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    private String name;
    private Integer order;

    @JsonProperty("original_name")
    private String originalName;

    private Double popularity;

    @JsonProperty("profile_path")
    private String profilePath;
}
