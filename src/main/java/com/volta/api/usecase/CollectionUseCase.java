package com.volta.api.usecase;

import com.volta.api.dto.collection.CollectionRequestDTO;
import com.volta.api.dto.collection.CollectionScheduleRequestDTO;
import com.volta.api.dto.collection.CollectionStatusRequestDTO;
import com.volta.api.dto.collection.CollectionCompletionTimeResponseDTO;
import com.volta.api.dto.collection.CollectionResponseDTO;
import com.volta.api.dto.collection.CollectionStatusResponseDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.List;
import java.util.UUID;

public interface CollectionUseCase {

    CollectionResponseDTO register(CollectionRequestDTO dto, AuthenticatedUser author);

    List<CollectionResponseDTO> getCollections(AuthenticatedUser author);

    CollectionResponseDTO getCollectionById(UUID id, AuthenticatedUser author);

    void schedule(UUID id, CollectionScheduleRequestDTO dto, AuthenticatedUser author);

    void updateStatus(UUID id, CollectionStatusRequestDTO dto, AuthenticatedUser author);

    CollectionCompletionTimeResponseDTO getCompletionTime(UUID id, AuthenticatedUser author);

    List<CollectionStatusResponseDTO> getCollectionStatus(UUID collectionId, AuthenticatedUser author);
}
