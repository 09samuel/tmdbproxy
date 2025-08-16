package com.sastudios.tmdbproxy.dto.seriesDetails.watchProvider;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Provider {
    @JsonProperty("logo_path")
    private String logoPath;

    @JsonProperty("provider_id")
    private Integer providerId;

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("display_priority")
    private Integer displayPriority;
}
