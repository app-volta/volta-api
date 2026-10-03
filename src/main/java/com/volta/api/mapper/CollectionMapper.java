package com.volta.api.mapper;

import com.volta.api.database.entity.Collection;
import com.volta.api.database.entity.Cooperative;
import com.volta.api.database.entity.Incident;
import com.volta.api.dto.request.CollectionRequestDTO;
import com.volta.api.dto.response.CollectionResponseDTO;
import com.volta.api.enums.CollectionStatusType;
import org.springframework.stereotype.Component;

@Component
public class CollectionMapper {

    public Collection toEntity(
            CollectionRequestDTO dto,
            Incident incident,
            Cooperative cooperative
    ) {
        Collection collection = new Collection();

        collection.setIncident(incident);
        collection.setCooperative(cooperative);
        collection.setCollectionType(dto.collectionType());
        collection.setUrgent(dto.urgent());
        collection.setCurrentStatus(CollectionStatusType.REQUESTED.name());

        return collection;
    }

    public CollectionResponseDTO toResponse(Collection collection) {
        return new CollectionResponseDTO(
                collection.getId(),
                collection.getIncident().getId(),
                collection.getCooperative().getId(),
                collection.getRequestedAt(),
                collection.getScheduledAt(),
                collection.getCurrentStatus(),
                collection.getCollectionType(),
                collection.isUrgent()
        );
    }
}