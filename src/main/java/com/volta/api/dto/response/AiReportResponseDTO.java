package com.volta.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record AiReportResponseDTO(
        UUID id,
        UUID incidentId,
        String detectedWasteType,
        String aiContaminationLevel,
        String recommendations,
        String reportText,
        LocalDateTime generatedAt
) {}
