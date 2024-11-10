package com.example.crm_bl.dtos.responses;

import java.util.List;

public record ResponseGroupDTO(Long id, String name, List<ResponseArtistDTO> artists) {
}
