package com.volta.api.service;

import com.volta.api.database.entity.WasteType;
import com.volta.api.database.repository.WasteTypeRepository;
import com.volta.api.dto.response.WasteTypeResponseDTO;
import com.volta.api.enums.RiskLevel;
import com.volta.api.enums.WasteCategory;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.WasteTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.volta.api.support.WasteTypeFixtures.entity;
import static com.volta.api.support.WasteTypeFixtures.request;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("WasteTypeService")
class WasteTypeServiceTest {

    private static final UUID ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    @Mock
    private WasteTypeRepository wasteTypeRepository;

    private WasteTypeService service;

    @BeforeEach
    void setUp() {
        service = new WasteTypeService(wasteTypeRepository, new WasteTypeMapper());
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("salva a entidade e guarda a categoria e o nível de risco como texto")
        void register_validRequest_savesEntityAndStoresEnumsAsText() {
            when(wasteTypeRepository.save(any(WasteType.class))).thenAnswer(invocation -> {
                WasteType saved = invocation.getArgument(0);
                saved.setId(ID);
                return saved;
            });

            WasteTypeResponseDTO response = service.register(request(WasteCategory.PLASTICO, RiskLevel.LOW));

            assertThat(response.id()).isEqualTo(ID);
            assertThat(response.category()).isEqualTo("PLASTICO");
            assertThat(response.defaultRiskLevel()).isEqualTo("LOW");

            ArgumentCaptor<WasteType> captor = ArgumentCaptor.forClass(WasteType.class);
            verify(wasteTypeRepository).save(captor.capture());
            assertThat(captor.getValue().getDescription()).isEqualTo("Test description");
        }

        @Test
        @DisplayName("categoria comum devolve a cor do coletor e a classe II")
        void register_nonHazardousCategory_returnsCollectorColorAndClassII() {
            when(wasteTypeRepository.save(any(WasteType.class))).thenAnswer(invocation -> invocation.getArgument(0));

            WasteTypeResponseDTO response = service.register(request(WasteCategory.PLASTICO, RiskLevel.LOW));

            assertThat(response.color()).isEqualTo("Vermelho");
            assertThat(response.wasteClass()).isEqualTo("CLASSE_II");
        }

        @ParameterizedTest(name = "{0} com risco HIGH é aceita e vira classe I")
        @EnumSource(value = WasteCategory.class, names = {"PERIGOSO", "SAUDE", "RADIOATIVO"})
        @DisplayName("categoria perigosa com risco HIGH é classe I")
        void register_hazardousCategoryWithHighRisk_returnsClassI(WasteCategory category) {
            when(wasteTypeRepository.save(any(WasteType.class))).thenAnswer(invocation -> invocation.getArgument(0));

            WasteTypeResponseDTO response = service.register(request(category, RiskLevel.HIGH));

            assertThat(response.wasteClass()).isEqualTo("CLASSE_I");
        }

        @ParameterizedTest(name = "{0} com risco {1} é rejeitada")
        @CsvSource({
                "PERIGOSO, LOW",
                "PERIGOSO, MEDIUM",
                "SAUDE, LOW",
                "SAUDE, MEDIUM",
                "RADIOATIVO, LOW",
                "RADIOATIVO, MEDIUM"
        })
        @DisplayName("categoria perigosa sem risco HIGH lança regra de negócio e não salva")
        void register_hazardousCategoryWithoutHighRisk_throwsBusinessRuleAndDoesNotSave(
                WasteCategory category, RiskLevel riskLevel) {
            assertThatThrownBy(() -> service.register(request(category, riskLevel)))
                    .isInstanceOf(BusinessRuleException.class)
                    .hasMessage("Category " + category.name() + " requires HIGH risk level");

            verify(wasteTypeRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getWasteTypes")
    class GetWasteTypes {

        @Test
        @DisplayName("converte todos os registros")
        void getWasteTypes_existingRecords_convertsAll() {
            when(wasteTypeRepository.findAll()).thenReturn(List.of(
                    entity(UUID.randomUUID(), WasteCategory.PLASTICO, RiskLevel.LOW),
                    entity(UUID.randomUUID(), WasteCategory.PERIGOSO, RiskLevel.HIGH)));

            List<WasteTypeResponseDTO> response = service.getWasteTypes();

            assertThat(response).extracting(WasteTypeResponseDTO::category)
                    .containsExactly("PLASTICO", "PERIGOSO");
        }

        @Test
        @DisplayName("sem registros devolve lista vazia")
        void getWasteTypes_noRecords_returnsEmptyList() {
            when(wasteTypeRepository.findAll()).thenReturn(List.of());

            assertThat(service.getWasteTypes()).isEmpty();
        }
    }

    @Nested
    @DisplayName("getWasteTypeById")
    class GetWasteTypeById {

        @Test
        @DisplayName("existente devolve o tipo de resíduo convertido")
        void getWasteTypeById_existing_returnsConvertedResponse() {
            when(wasteTypeRepository.findById(ID))
                    .thenReturn(Optional.of(entity(ID, WasteCategory.VIDRO, RiskLevel.LOW)));

            WasteTypeResponseDTO response = service.getWasteTypeById(ID);

            assertThat(response.id()).isEqualTo(ID);
            assertThat(response.category()).isEqualTo("VIDRO");
            assertThat(response.color()).isEqualTo("Verde");
        }

        @Test
        @DisplayName("inexistente lança not found")
        void getWasteTypeById_missing_throwsNotFound() {
            when(wasteTypeRepository.findById(ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getWasteTypeById(ID))
                    .isInstanceOfSatisfying(ResourceNotFoundException.class, error -> {
                        assertThat(error).hasMessage("Waste type not found");
                        assertThat(error.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    });
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("existente altera os campos e salva")
        void update_existing_changesFieldsAndSaves() {
            WasteType existing = entity(ID, WasteCategory.PLASTICO, RiskLevel.LOW);
            when(wasteTypeRepository.findById(ID)).thenReturn(Optional.of(existing));

            WasteTypeResponseDTO response = service.update(ID, request(WasteCategory.METAL, RiskLevel.MEDIUM));

            assertThat(response.category()).isEqualTo("METAL");
            assertThat(response.defaultRiskLevel()).isEqualTo("MEDIUM");
            verify(wasteTypeRepository).save(existing);
        }

        @Test
        @DisplayName("inexistente lança not found e não salva")
        void update_missing_throwsNotFoundAndDoesNotSave() {
            when(wasteTypeRepository.findById(ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.update(ID, request(WasteCategory.METAL, RiskLevel.LOW)))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(wasteTypeRepository, never()).save(any());
        }

        @Test
        @DisplayName("categoria perigosa sem risco HIGH lança regra de negócio e não salva")
        void update_hazardousCategoryWithoutHighRisk_throwsBusinessRuleAndDoesNotSave() {
            WasteType existing = entity(ID, WasteCategory.PLASTICO, RiskLevel.LOW);
            when(wasteTypeRepository.findById(ID)).thenReturn(Optional.of(existing));

            assertThatThrownBy(() -> service.update(ID, request(WasteCategory.SAUDE, RiskLevel.LOW)))
                    .isInstanceOf(BusinessRuleException.class)
                    .hasMessage("Category SAUDE requires HIGH risk level");

            assertThat(existing.getCategory()).isEqualTo("PLASTICO");
            verify(wasteTypeRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("existente remove pelo id")
        void delete_existing_removesById() {
            when(wasteTypeRepository.existsById(ID)).thenReturn(true);

            service.delete(ID);

            verify(wasteTypeRepository).deleteById(ID);
        }

        @Test
        @DisplayName("inexistente lança not found e não apaga")
        void delete_missing_throwsNotFoundAndDoesNotDelete() {
            when(wasteTypeRepository.existsById(ID)).thenReturn(false);

            assertThatThrownBy(() -> service.delete(ID))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(wasteTypeRepository, never()).deleteById(any());
        }
    }
}
