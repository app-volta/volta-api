package com.volta.api.service;

import com.volta.api.database.entity.Cooperative;
import com.volta.api.database.repository.CooperativeRepository;
import com.volta.api.dto.cooperative.CooperativeRequestDTO;
import com.volta.api.dto.cooperative.CooperativeResponseDTO;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.InvalidRequestException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.CooperativeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CooperativeService")
class CooperativeServiceTest {

    private static final UUID ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    @Mock
    private CooperativeRepository cooperativeRepository;

    private CooperativeService service;

    @BeforeEach
    void setUp() {
        service = new CooperativeService(cooperativeRepository, new CooperativeMapper());
    }

    private CooperativeRequestDTO request(String specialties) {
        return new CooperativeRequestDTO(
                "Recicla Mais",
                "11.444.777/0001-61",
                new BigDecimal("-23.561684"),
                new BigDecimal("-46.655981"),
                specialties);
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("especialidades válidas salvam a cooperativa com o texto normalizado")
        void register_validSpecialties_savesNormalizedSpecialties() {
            when(cooperativeRepository.existsByCnpj("11444777000161")).thenReturn(false);
            when(cooperativeRepository.save(any(Cooperative.class))).thenAnswer(invocation -> invocation.getArgument(0));

            CooperativeResponseDTO response = service.register(request("papel, Plástico, não reciclável"));

            assertThat(response.specialties()).isEqualTo("PAPEL,PLASTICO,NAO_RECICLAVEL");
            ArgumentCaptor<Cooperative> captor = ArgumentCaptor.forClass(Cooperative.class);
            verify(cooperativeRepository).save(captor.capture());
            assertThat(captor.getValue().getCnpj()).isEqualTo("11444777000161");
        }

        @ParameterizedTest(name = "\"{0}\" é rejeitada")
        @ValueSource(strings = {"BORRACHA", "PAPEL,BORRACHA", "papel, tecido"})
        @DisplayName("especialidade desconhecida lança requisição inválida e não consulta nem salva")
        void register_unknownSpecialty_throwsInvalidRequestAndDoesNotSave(String specialties) {
            assertThatThrownBy(() -> service.register(request(specialties)))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("Invalid specialty")
                    .hasMessageContaining("PAPEL")
                    .hasMessageContaining("NAO_RECICLAVEL");

            verifyNoInteractions(cooperativeRepository);
        }

        @Test
        @DisplayName("mensagem aponta a especialidade inválida")
        void register_unknownSpecialty_messageNamesTheInvalidValue() {
            assertThatThrownBy(() -> service.register(request("PAPEL,tecido")))
                    .hasMessageContaining("'TECIDO'");
        }

        @Test
        @DisplayName("CNPJ já cadastrado lança conflito e não salva")
        void register_cnpjAlreadyRegistered_throwsConflictAndDoesNotSave() {
            when(cooperativeRepository.existsByCnpj("11444777000161")).thenReturn(true);

            assertThatThrownBy(() -> service.register(request("PAPEL")))
                    .isInstanceOf(ConflictException.class)
                    .hasMessage("CNPJ already registered");

            verify(cooperativeRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("especialidade desconhecida lança requisição inválida antes de consultar o banco")
        void update_unknownSpecialty_throwsInvalidRequestWithoutQueryingRepository() {
            assertThatThrownBy(() -> service.update(ID, request("BORRACHA")))
                    .isInstanceOf(InvalidRequestException.class);

            verifyNoInteractions(cooperativeRepository);
        }

        @Test
        @DisplayName("cooperativa inexistente lança not found")
        void update_missingCooperative_throwsNotFound() {
            when(cooperativeRepository.findById(ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.update(ID, request("PAPEL")))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Cooperative not found");

            verify(cooperativeRepository, never()).save(any());
        }

        @Test
        @DisplayName("CNPJ de outra cooperativa lança conflito")
        void update_cnpjOfAnotherCooperative_throwsConflict() {
            when(cooperativeRepository.findById(ID)).thenReturn(Optional.of(new Cooperative()));
            when(cooperativeRepository.existsByCnpjAndIdNot("11444777000161", ID)).thenReturn(true);

            assertThatThrownBy(() -> service.update(ID, request("PAPEL")))
                    .isInstanceOf(ConflictException.class);

            verify(cooperativeRepository, never()).save(any());
        }

        @Test
        @DisplayName("dados válidos alteram os campos e salvam")
        void update_validData_changesFieldsAndSaves() {
            Cooperative existing = new Cooperative();
            existing.setId(ID);
            when(cooperativeRepository.findById(ID)).thenReturn(Optional.of(existing));

            CooperativeResponseDTO response = service.update(ID, request("vidro,METAL"));

            assertThat(response.specialties()).isEqualTo("VIDRO,METAL");
            assertThat(existing.getName()).isEqualTo("Recicla Mais");
            verify(cooperativeRepository).save(existing);
        }
    }
}
