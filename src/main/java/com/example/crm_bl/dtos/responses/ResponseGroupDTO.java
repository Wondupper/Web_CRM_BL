package com.example.crm_bl.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public record ResponseGroupDTO(Long id, String name, ResponseGenreDTO genre, List<ResponseArtistDTO> artists, List<ResponseTrackDTO> tracks) {
}
