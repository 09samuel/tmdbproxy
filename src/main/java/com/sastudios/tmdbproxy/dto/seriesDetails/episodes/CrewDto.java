package com.sastudios.tmdbproxy.dto.seriesDetails.episodes;

import lombok.Data;

@Data
public class CrewDto {
    private Boolean adult;
    private String creditId;
    private String department;
    private Integer gender;
    private Integer id;
    private String job;
    private String knownForDepartment;
    private String name;
    private String originalName;
    private Double popularity;
    private String profilePath;
}
