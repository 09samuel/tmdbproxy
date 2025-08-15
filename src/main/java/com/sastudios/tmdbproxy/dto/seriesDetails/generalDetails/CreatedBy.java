package com.sastudios.tmdbproxy.dto.seriesDetails.generalDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreatedBy {
    @JsonProperty("credit_id")
    private String creditId;
    private Integer gender;
    private Integer id;
    private String name;
    @JsonProperty("original_name")
    private String originalName;
    @JsonProperty("profile_path")
    private String profilePath;
}