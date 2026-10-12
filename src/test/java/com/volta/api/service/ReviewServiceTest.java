package com.volta.api.service;

import com.volta.api.database.entity.Collection;
import com.volta.api.database.entity.Cooperative;
import com.volta.api.database.entity.Review;
import com.volta.api.database.entity.Users;
import com.volta.api.database.repository.CollectionRepository;
import com.volta.api.database.repository.CooperativeRepository;
import com.volta.api.database.repository.ReviewRepository;
import com.volta.api.database.repository.UserRepository;
import com.volta.api.dto.review.ReviewRequestDTO;
import com.volta.api.dto.review.ReviewResponseDTO;
import com.volta.api.enums.CollectionStatusType;
import com.volta.api.enums.IncidentStatus;
import com.volta.api.enums.Priority;
import com.volta.api.enums.RiskLevel;
import com.volta.api.enums.WasteCategory;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.ReviewMapper;
import com.volta.api.security.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.volta.api.support.CollectionFixtures.collection;
import static com.volta.api.support.CollectionFixtures.cooperative;
import static com.volta.api.support.CollectionFixtures.incident;
import static com.volta.api.support.CollectionFixtures.wasteType;
import static com.volta.api.support.UserFixtures.authenticatedUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService")
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private CooperativeRepository cooperativeRepository;

    @Mock
    private UserRepository userRepository;

    private ReviewService service;

    private final UUID companyId = UUID.randomUUID();

    private final AuthenticatedUser author = authenticatedUser(UUID.randomUUID(), companyId, "MANAGER");

    @BeforeEach
    void setUp() {
        service = new ReviewService(
                reviewRepository,
                collectionRepository,
                cooperativeRepository,
                userRepository,
                new ReviewMapper());
    }

    private Collection collectionIn(CollectionStatusType status) {
        Collection collection = collection(
                incident(wasteType(WasteCategory.PLASTICO, RiskLevel.LOW), Priority.MEDIUM, IncidentStatus.CLOSED),
                cooperative("PLASTICO"),
                status,
                LocalDateTime.of(2026, 10, 1, 9, 0),
                false);
        when(collectionRepository.findByIdAndIncidentCompanyId(collection.getId(), companyId))
                .thenReturn(Optional.of(collection));
        return collection;
    }

    private Users authorUser() {
        Users user = new Users();
        user.setId(author.id());
        return user;
    }

    @Nested
    @DisplayName("register")
    class Register {

        private final ReviewRequestDTO request = new ReviewRequestDTO(5, "Great service");

        @Test
        @DisplayName("coleta de outra empresa lança not found")
        void register_collectionOfAnotherCompany_throwsNotFound() {
            UUID id = UUID.randomUUID();
            when(collectionRepository.findByIdAndIncidentCompanyId(id, companyId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.register(id, request, author))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Collection not found");

            verify(reviewRepository, never()).saveAndFlush(any());
        }

        @Test
        @DisplayName("coleta não concluída lança regra de negócio")
        void register_notCompletedCollection_throwsBusinessRule() {
            Collection collection = collectionIn(CollectionStatusType.IN_PROGRESS);

            assertThatThrownBy(() -> service.register(collection.getId(), request, author))
                    .isInstanceOf(BusinessRuleException.class)
                    .hasMessage("Only completed collections can be reviewed");

            verify(reviewRepository, never()).saveAndFlush(any());
        }

        @Test
        @DisplayName("coleta já avaliada lança conflito")
        void register_alreadyReviewedCollection_throwsConflict() {
            Collection collection = collectionIn(CollectionStatusType.COMPLETED);
            when(reviewRepository.existsByCollectionId(collection.getId())).thenReturn(true);

            assertThatThrownBy(() -> service.register(collection.getId(), request, author))
                    .isInstanceOf(ConflictException.class)
                    .hasMessage("Collection already reviewed");

            verify(reviewRepository, never()).saveAndFlush(any());
        }

        @Test
        @DisplayName("usuário inexistente lança not found")
        void register_missingUser_throwsNotFound() {
            Collection collection = collectionIn(CollectionStatusType.COMPLETED);
            when(reviewRepository.existsByCollectionId(collection.getId())).thenReturn(false);
            when(userRepository.findById(author.id())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.register(collection.getId(), request, author))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("User not found");
        }

        @Test
        @DisplayName("dados válidos salvam a avaliação vinculada à coleta, cooperativa e usuário")
        void register_validData_savesReviewLinkedToCollectionCooperativeAndUser() {
            Collection collection = collectionIn(CollectionStatusType.COMPLETED);
            when(reviewRepository.existsByCollectionId(collection.getId())).thenReturn(false);
            when(userRepository.findById(author.id())).thenReturn(Optional.of(authorUser()));
            when(reviewRepository.saveAndFlush(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reviewRepository.findAverageStarsByCooperativeId(collection.getCooperative().getId())).thenReturn(5.0);

            ReviewResponseDTO response = service.register(collection.getId(), request, author);

            assertThat(response.stars()).isEqualTo(5);
            assertThat(response.comment()).isEqualTo("Great service");
            assertThat(response.collectionId()).isEqualTo(collection.getId());
            assertThat(response.cooperativeId()).isEqualTo(collection.getCooperative().getId());
            assertThat(response.userId()).isEqualTo(author.id());
        }

        @Test
        @DisplayName("recalcula e salva a média de notas da cooperativa")
        void register_validData_updatesCooperativeAverageRating() {
            Collection collection = collectionIn(CollectionStatusType.COMPLETED);
            Cooperative cooperative = collection.getCooperative();
            when(reviewRepository.existsByCollectionId(collection.getId())).thenReturn(false);
            when(userRepository.findById(author.id())).thenReturn(Optional.of(authorUser()));
            when(reviewRepository.saveAndFlush(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reviewRepository.findAverageStarsByCooperativeId(cooperative.getId())).thenReturn(4.5);

            service.register(collection.getId(), request, author);

            ArgumentCaptor<Cooperative> captor = ArgumentCaptor.forClass(Cooperative.class);
            verify(cooperativeRepository).save(captor.capture());
            assertThat(captor.getValue().getAverageRating()).isEqualByComparingTo(new BigDecimal("4.5"));
        }

        @Test
        @DisplayName("sem média calculada a nota da cooperativa volta a zero")
        void register_nullAverage_setsCooperativeRatingToZero() {
            Collection collection = collectionIn(CollectionStatusType.COMPLETED);
            Cooperative cooperative = collection.getCooperative();
            when(reviewRepository.existsByCollectionId(collection.getId())).thenReturn(false);
            when(userRepository.findById(author.id())).thenReturn(Optional.of(authorUser()));
            when(reviewRepository.saveAndFlush(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reviewRepository.findAverageStarsByCooperativeId(cooperative.getId())).thenReturn(null);

            service.register(collection.getId(), request, author);

            assertThat(cooperative.getAverageRating()).isEqualByComparingTo(BigDecimal.ZERO);
            verify(cooperativeRepository).save(cooperative);
        }
    }

    @Nested
    @DisplayName("getReviewsByCooperative")
    class GetReviewsByCooperative {

        @Test
        @DisplayName("cooperativa inexistente lança not found sem consultar avaliações")
        void getReviewsByCooperative_missingCooperative_throwsNotFound() {
            UUID id = UUID.randomUUID();
            when(cooperativeRepository.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> service.getReviewsByCooperative(id))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Cooperative not found");

            verify(reviewRepository, never()).findByCooperativeIdOrderByReviewedAtDesc(any());
        }

        @Test
        @DisplayName("cooperativa existente devolve as avaliações convertidas")
        void getReviewsByCooperative_existingCooperative_returnsConvertedReviews() {
            Cooperative cooperative = cooperative("PLASTICO");
            Collection collection = collection(
                    incident(wasteType(WasteCategory.PLASTICO, RiskLevel.LOW), Priority.LOW, IncidentStatus.CLOSED),
                    cooperative,
                    CollectionStatusType.COMPLETED,
                    LocalDateTime.of(2026, 10, 1, 9, 0),
                    false);
            Review review = new ReviewMapper().toEntity(new ReviewRequestDTO(4, "Good"), collection, authorUser());
            review.setId(UUID.randomUUID());
            when(cooperativeRepository.existsById(cooperative.getId())).thenReturn(true);
            when(reviewRepository.findByCooperativeIdOrderByReviewedAtDesc(cooperative.getId()))
                    .thenReturn(List.of(review));

            List<ReviewResponseDTO> response = service.getReviewsByCooperative(cooperative.getId());

            assertThat(response).hasSize(1);
            assertThat(response.get(0).id()).isEqualTo(review.getId());
            assertThat(response.get(0).stars()).isEqualTo(4);
        }
    }
}
