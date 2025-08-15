package com.sastudios.tmdbproxy.dto.seriesDetails.recommendations;

import com.sastudios.tmdbproxy.dto.seriesList.SeriesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationsListDto {
    private Integer page;
    private List<RecommendationsDto> results;
    private Integer totalPages;
    private Integer totalResults;
}
