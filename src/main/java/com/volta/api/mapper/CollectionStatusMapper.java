package com.volta.api.mapper;

import com.volta.api.database.entity.CollectionStatus;
import com.volta.api.dto.response.CollectionStatusResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CollectionStatusMapper {

    public CollectionStatusResponseDTO toResponse(CollectionStatus collectionStatus) {
        return new CollectionStatusResponseDTO(
                collectionStatus.getId(),
                collectionStatus.getCollection().getId(),
                collectionStatus.getStatus(),
                collectionStatus.getChangedAt(),
                collectionStatus.getObservation()
        );
    }
}