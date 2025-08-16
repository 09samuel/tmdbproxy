package com.sastudios.tmdbproxy.dto.seriesDetails.episodes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CrewDto {
    private Boolean adult;

    @JsonProperty("credit_id")
    private String creditId;

    private String department;
    private Integer gender;
    private Integer id;
    private String job;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    private String name;

    @JsonProperty("original_name")
    private String originalName;

    private Double popularity;

    @JsonProperty("profile_path")
    private String profilePath;
}