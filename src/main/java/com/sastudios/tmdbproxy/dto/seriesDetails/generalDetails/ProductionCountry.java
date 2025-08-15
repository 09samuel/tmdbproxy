package com.sastudios.tmdbproxy.dto.seriesDetails.generalDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductionCountry {
    @JsonProperty("iso_3166_1")
    private String iso3166_1;
    private String name;
}