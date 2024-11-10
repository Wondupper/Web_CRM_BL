package com.example.crm_bl.dtos.requests;

import java.time.LocalDateTime;

public record RequestScheduleDTO(LocalDateTime time, RequestTrackDTO track) {
}
