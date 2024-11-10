package com.example.crm_bl.dtos.responses;

import java.time.LocalDateTime;

public record ResponseScheduleDTO(Long id, LocalDateTime time, ResponseTrackDTO track) {
}
