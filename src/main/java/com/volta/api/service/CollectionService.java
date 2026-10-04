package com.volta.api.service;

import com.volta.api.database.entity.Collection;
import com.volta.api.database.entity.CollectionStatus;
import com.volta.api.database.entity.Cooperative;
import com.volta.api.database.entity.Incident;
import com.volta.api.database.function.CollectionFunction;
import com.volta.api.database.procedure.CollectionProcedure;
import com.volta.api.database.repository.CollectionRepository;
import com.volta.api.database.repository.CollectionStatusRepository;
import com.volta.api.database.repository.CooperativeRepository;
import com.volta.api.database.repository.IncidentRepository;
import com.volta.api.dto.request.CollectionRequestDTO;
import com.volta.api.dto.request.CollectionScheduleRequestDTO;
import com.volta.api.dto.request.CollectionStatusRequestDTO;
import com.volta.api.dto.response.CollectionCompletionTimeResponseDTO;
import com.volta.api.dto.response.CollectionResponseDTO;
import com.volta.api.dto.response.CollectionStatusResponseDTO;
import com.volta.api.enums.CollectionStatusType;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.CollectionMapper;
import com.volta.api.mapper.CollectionStatusMapper;
import com.volta.api.security.AuthenticatedUser;
import com.volta.api.usecase.CollectionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollectionService implements CollectionUseCase {

    private final CollectionRepository collectionRepository;
    private final CollectionProcedure collectionProcedure;
    private final CollectionFunction collectionFunction;
    private final IncidentRepository incidentRepository;
    private final CooperativeRepository cooperativeRepository;
    private final CollectionMapper collectionMapper;
    private final CollectionStatusRepository collectionStatusRepository;
    private final CollectionStatusMapper collectionStatusMapper;

    @Transactional
    public CollectionResponseDTO register(CollectionRequestDTO dto, AuthenticatedUser author) {
        Incident incident = incidentRepository.findByIdAndCompanyId(dto.incidentId(), author.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Incident"));
        Cooperative cooperative = cooperativeRepository.findById(dto.cooperativeId())
                .orElseThrow(() -> new ResourceNotFoundException("Cooperative"));

        Collection collection = collectionMapper.toEntity(dto, incident, cooperative);
        Collection saved = collectionRepository.saveAndFlush(collection);

        collectionProcedure.updateCollectionStatus(
                saved.getId(),
                CollectionStatusType.REQUESTED.name(),
                "Collection Requested"
        );

        return collectionMapper.toResponse(saved);
    }

    public List<CollectionResponseDTO> getCollections(AuthenticatedUser author){
        List<CollectionResponseDTO> collections = new ArrayList<>();

        for (Collection collection : collectionRepository.findByIncidentCompanyId(author.companyId())){
            collections.add(collectionMapper.toResponse(collection));
        }

        return collections;
    }

    public CollectionResponseDTO getCollectionById(UUID id, AuthenticatedUser author){
        Collection collection = findOwnedCollection(id, author);
        return collectionMapper.toResponse(collection);
    }

    public List<CollectionStatusResponseDTO> getCollectionStatus(UUID collectionId, AuthenticatedUser author){
        findOwnedCollection(collectionId, author);

        List<CollectionStatusResponseDTO> collectionStatuses = new ArrayList<>();

        for (CollectionStatus collectionStatus : collectionStatusRepository.findByCollectionIdOrderByChangedAtAsc(collectionId)){
            collectionStatuses.add(collectionStatusMapper.toResponse(collectionStatus));
        }

        return collectionStatuses;
    }

    @Transactional
    public void schedule(UUID id, CollectionScheduleRequestDTO dto, AuthenticatedUser author) {
        Collection collection = findOwnedCollection(id, author);
        validateNotFinished(collection);
        collectionProcedure.scheduleCollection(id, dto.scheduledAt());
    }

    @Transactional
    public void updateStatus(UUID id, CollectionStatusRequestDTO dto, AuthenticatedUser author) {
        Collection collection = findOwnedCollection(id, author);
        validateNotFinished(collection);

        if (dto.status() == CollectionStatusType.SCHEDULED) {
            throw new BusinessRuleException("Use the schedule endpoint to schedule a collection");
        }

        collectionProcedure.updateCollectionStatus(id, dto.status().name(), dto.observation());
    }

    public CollectionCompletionTimeResponseDTO getCompletionTime(UUID id, AuthenticatedUser author) {
        Collection collection = findOwnedCollection(id, author);

        if (!CollectionStatusType.COMPLETED.name().equals(collection.getCurrentStatus())) {
            throw new BusinessRuleException("Collection is not completed yet");
        }

        return new CollectionCompletionTimeResponseDTO(
                id,
                collectionFunction.calculateCollectionCompletionHours(id)
        );
    }

    private Collection findOwnedCollection(UUID id, AuthenticatedUser author) {
        return collectionRepository.findByIdAndIncidentCompanyId(id, author.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Collection"));
    }

    private void validateNotFinished(Collection collection) {
        String current = collection.getCurrentStatus();
        if (CollectionStatusType.COMPLETED.name().equals(current)
                || CollectionStatusType.CANCELED.name().equals(current)) {
            throw new BusinessRuleException("Collection is already finished");
        }
    }
}
