package com.volta.api.usecase;

import com.volta.api.dto.request.CollectionRequestDTO;
import com.volta.api.dto.request.CollectionScheduleRequestDTO;
import com.volta.api.dto.request.CollectionStatusRequestDTO;
import com.volta.api.dto.response.CollectionResponseDTO;
import com.volta.api.security.AuthenticatedUser;

import java.util.List;
import java.util.UUID;

public interface CollectionUseCase {

    CollectionResponseDTO register(CollectionRequestDTO dto, AuthenticatedUser author);

    List<CollectionResponseDTO> getCollections(AuthenticatedUser author);

    CollectionResponseDTO getCollectionById(UUID id, AuthenticatedUser author);

    void schedule(UUID id, CollectionScheduleRequestDTO dto, AuthenticatedUser author);

    void updateStatus(UUID id, CollectionStatusRequestDTO dto, AuthenticatedUser author);
}
