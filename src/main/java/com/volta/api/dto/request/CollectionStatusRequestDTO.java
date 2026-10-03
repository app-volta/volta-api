package com.volta.api.dto.request;

import com.volta.api.enums.CollectionStatusType;
import jakarta.validation.constraints.NotNull;

public record CollectionStatusRequestDTO(

        @NotNull
        CollectionStatusType status,

        String observation
) {}
