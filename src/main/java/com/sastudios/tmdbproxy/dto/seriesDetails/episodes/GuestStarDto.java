package com.sastudios.tmdbproxy.dto.seriesDetails.episodes;

import lombok.Data;

@Data
public class GuestStarDto {
    private Boolean adult;
    private String character;
    private String creditId;
    private Integer gender;
    private Integer id;
    private String knownForDepartment;
    private String name;
    private Integer order;
    private String originalName;
    private Double popularity;
    private String profilePath;
}
