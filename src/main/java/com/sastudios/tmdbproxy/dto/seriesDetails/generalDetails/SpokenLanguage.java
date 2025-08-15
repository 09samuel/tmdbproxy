package com.sastudios.tmdbproxy.dto.seriesDetails.generalDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpokenLanguage {
    @JsonProperty("english_name")
    private String englishName;
    @JsonProperty("iso_639_1")
    private String iso639_1;
    private String name;
}