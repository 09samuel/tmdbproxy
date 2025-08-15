package com.sastudios.tmdbproxy.dto.seriesDetails.episodes;

import com.sastudios.tmdbproxy.dto.seriesList.SeriesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodesListDto {
    private String _id;
    private String airDate;
    private List<EpisodeDto> episodes;
    private Integer id;
    private String name;

    // Optional season details (uncomment if needed)
    // private String overview;
    // private String posterPath;
    // private Integer seasonNumber;
    // private Double voteAverage;

}
