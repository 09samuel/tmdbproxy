package com.sastudios.tmdbproxy.dto.seriesDetails.watchProvider;

import lombok.Data;

@Data
public class Provider {
    private String logoPath;
    private Integer providerId;
    private String providerName;
    private Integer displayPriority;
}