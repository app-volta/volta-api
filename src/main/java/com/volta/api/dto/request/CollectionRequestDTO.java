package com.volta.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CollectionRequestDTO(

        @NotNull
        UUID incidentId,

        @NotNull
        UUID cooperativeId,

        @NotBlank
        @Size(max = 50)
        String collectionType,

        boolean urgent
) {
}
