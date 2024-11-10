package com.example.crm_bl.dtos.requests;

import java.util.List;

public record RequestGroupDTO(String name, List<RequestArtistDTO> artists) {
}
