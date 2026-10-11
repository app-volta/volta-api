package com.volta.api.service;

import com.volta.api.database.entity.Area;
import com.volta.api.database.entity.Company;
import com.volta.api.database.entity.Incident;
import com.volta.api.database.entity.Users;
import com.volta.api.database.entity.WasteType;
import com.volta.api.database.procedure.IncidentProcedure;
import com.volta.api.database.repository.AiReportRepository;
import com.volta.api.database.repository.AreaRepository;
import com.volta.api.database.repository.CollectionRepository;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.database.repository.IncidentRepository;
import com.volta.api.database.repository.UserRepository;
import com.volta.api.database.repository.WasteTypeRepository;
import com.volta.api.dto.incident.IncidentFilterDTO;
import com.volta.api.dto.incident.IncidentRequestDTO;
import com.volta.api.dto.incident.IncidentResponseDTO;
import com.volta.api.enums.CollectionStatusType;
import com.volta.api.enums.IncidentStatus;
import com.volta.api.enums.Priority;
import com.volta.api.enums.RiskLevel;
import com.volta.api.enums.WasteCategory;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.AiReportMapper;
import com.volta.api.mapper.IncidentMapper;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.volta.api.support.CollectionFixtures.incident;
import static com.volta.api.support.CollectionFixtures.wasteType;
import static com.volta.api.support.UserFixtures.authenticatedUser;
import static com.volta.api.support.UserFixtures.company;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("IncidentService")
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private WasteTypeRepository wasteTypeRepository;

    @Mock
    private IncidentProcedure incidentProcedure;

    @Mock
    private AiReportRepository aiReportRepository;

    @Mock
    private CollectionRepository collectionRepository;

    private IncidentService service;

    private final UUID companyId = UUID.randomUUID();

    private final AuthenticatedUser author = authenticatedUser(UUID.randomUUID(), companyId, "EMPLOYEE");

    @BeforeEach
    void setUp() {
        service = new IncidentService(
                incidentRepository,
                companyRepository,
                userRepository,
                areaRepository,
                wasteTypeRepository,
                new IncidentMapper(),
                incidentProcedure,
                aiReportRepository,
                new AiReportMapper(),
                collectionRepository);
    }

    @Nested
    @DisplayName("register")
    class Register {

        private Company company;
        private Users user;
        private Area area;

        @BeforeEach
        void givenAuthorData() {
            company = company(companyId);
            user = new Users();
            user.setId(author.id());
            area = new Area();
            area.setId(UUID.randomUUID());
            area.setCompany(company);
        }

        private void givenRegistrationData(WasteType wasteType) {
            when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
            when(userRepository.findById(author.id())).thenReturn(Optional.of(user));
            when(areaRepository.findByIdAndCompanyId(area.getId(), companyId)).thenReturn(Optional.of(area));
            when(wasteTypeRepository.findById(wasteType.getId())).thenReturn(Optional.of(wasteType));
            when(incidentRepository.saveAndFlush(any(Incident.class))).thenAnswer(invocation -> invocation.getArgument(0));
        }

        private IncidentRequestDTO request(WasteType wasteType, RiskLevel contamination, Priority priority) {
            return new IncidentRequestDTO(
                    area.getId(),
                    wasteType.getId(),
                    null,
                    "Oil leak next to machine 3",
                    contamination,
                    new BigDecimal("10.00"),
                    priority);
        }

        @Test
        @DisplayName("área de outra empresa lança not found e não salva")
        void register_areaOfAnotherCompany_throwsNotFound() {
            when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
            when(userRepository.findById(author.id())).thenReturn(Optional.of(user));
            when(areaRepository.findByIdAndCompanyId(area.getId(), companyId)).thenReturn(Optional.empty());
            WasteType wasteType = wasteType(WasteCategory.PLASTICO, RiskLevel.LOW);

            assertThatThrownBy(() -> service.register(request(wasteType, null, Priority.LOW), author))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Area not found");

            verify(incidentRepository, never()).saveAndFlush(any());
        }

        @Test
        @DisplayName("sem contaminação informada assume o risco padrão do tipo de resíduo")
        void register_withoutContamination_usesWasteTypeDefaultRisk() {
            WasteType wasteType = wasteType(WasteCategory.PLASTICO, RiskLevel.MEDIUM);
            givenRegistrationData(wasteType);

            IncidentResponseDTO response = service.register(request(wasteType, null, Priority.LOW), author);

            assertThat(response.contaminationLevel()).isEqualTo("MEDIUM");
            assertThat(response.priority()).isEqualTo("LOW");
            assertThat(response.status()).isEqualTo(IncidentStatus.PENDING.name());
        }

        @Test
        @DisplayName("resíduo perigoso eleva a prioridade para HIGH")
        void register_hazardousWaste_raisesPriorityToHigh() {
            WasteType wasteType = wasteType(WasteCategory.PERIGOSO, RiskLevel.HIGH);
            givenRegistrationData(wasteType);

            IncidentResponseDTO response = service.register(request(wasteType, RiskLevel.LOW, Priority.LOW), author);

            assertThat(response.priority()).isEqualTo("HIGH");
        }

        @Test
        @DisplayName("contaminação HIGH eleva a prioridade para HIGH")
        void register_highContamination_raisesPriorityToHigh() {
            WasteType wasteType = wasteType(WasteCategory.PLASTICO, RiskLevel.LOW);
            givenRegistrationData(wasteType);

            IncidentResponseDTO response = service.register(request(wasteType, RiskLevel.HIGH, Priority.MEDIUM), author);

            assertThat(response.priority()).isEqualTo("HIGH");
        }

        @Test
        @DisplayName("prioridade CRITICAL não é rebaixada")
        void register_criticalPriority_isKept() {
            WasteType wasteType = wasteType(WasteCategory.PERIGOSO, RiskLevel.HIGH);
            givenRegistrationData(wasteType);

            IncidentResponseDTO response = service.register(request(wasteType, null, Priority.CRITICAL), author);

            assertThat(response.priority()).isEqualTo("CRITICAL");
        }

        @Test
        @DisplayName("salva o incidente vinculado à empresa e ao usuário autenticado")
        void register_validData_savesWithAuthorCompanyAndUser() {
            WasteType wasteType = wasteType(WasteCategory.PLASTICO, RiskLevel.LOW);
            givenRegistrationData(wasteType);

            service.register(request(wasteType, null, Priority.LOW), author);

            ArgumentCaptor<Incident> captor = ArgumentCaptor.forClass(Incident.class);
            verify(incidentRepository).saveAndFlush(captor.capture());
            assertThat(captor.getValue().getCompany()).isSameAs(company);
            assertThat(captor.getValue().getUser()).isSameAs(user);
        }
    }

    @Nested
    @DisplayName("getIncidents e getIncidentById")
    class Queries {

        @Test
        @DisplayName("busca sempre filtrando pela empresa do autor")
        void getIncidents_filtersByAuthorCompany() {
            when(incidentRepository.search(companyId, "PENDING", null, null)).thenReturn(List.of());

            List<IncidentResponseDTO> response = service.getIncidents(
                    new IncidentFilterDTO("PENDING", null, null), author);

            assertThat(response).isEmpty();
            verify(incidentRepository).search(companyId, "PENDING", null, null);
        }

        @Test
        @DisplayName("incidente de outra empresa lança not found")
        void getIncidentById_ofAnotherCompany_throwsNotFound() {
            UUID id = UUID.randomUUID();
            when(incidentRepository.findByIdAndCompanyId(id, companyId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getIncidentById(id, author))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Incident not found");
        }

        @Test
        @DisplayName("relatório de IA de incidente de outra empresa lança not found sem consultar relatório")
        void getAiReport_ofAnotherCompany_throwsNotFoundWithoutQueryingReport() {
            UUID id = UUID.randomUUID();
            when(incidentRepository.findByIdAndCompanyId(id, companyId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getAiReport(id, author))
                    .isInstanceOf(ResourceNotFoundException.class);

            verifyNoInteractions(aiReportRepository);
        }
    }

    @Nested
    @DisplayName("closeIncident")
    class CloseIncident {

        private Incident givenOwnedIncident(WasteType wasteType, IncidentStatus status) {
            Incident incident = incident(wasteType, Priority.MEDIUM, status);
            when(incidentRepository.findByIdAndCompanyId(incident.getId(), companyId)).thenReturn(Optional.of(incident));
            return incident;
        }

        @Test
        @DisplayName("incidente inexistente lança not found")
        void closeIncident_missingIncident_throwsNotFound() {
            UUID id = UUID.randomUUID();
            when(incidentRepository.findByIdAndCompanyId(id, companyId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.closeIncident(id, author))
                    .isInstanceOf(ResourceNotFoundException.class);

            verifyNoInteractions(incidentProcedure);
        }

        @Test
        @DisplayName("incidente já fechado lança regra de negócio")
        void closeIncident_alreadyClosed_throwsBusinessRule() {
            Incident incident = givenOwnedIncident(wasteType(WasteCategory.PLASTICO, RiskLevel.LOW), IncidentStatus.CLOSED);

            assertThatThrownBy(() -> service.closeIncident(incident.getId(), author))
                    .isInstanceOf(BusinessRuleException.class)
                    .hasMessage("Incident is already closed");

            verifyNoInteractions(incidentProcedure);
        }

        @Test
        @DisplayName("incidente com coleta ativa lança regra de negócio")
        void closeIncident_withActiveCollection_throwsBusinessRule() {
            Incident incident = givenOwnedIncident(wasteType(WasteCategory.PLASTICO, RiskLevel.LOW), IncidentStatus.PENDING);
            when(collectionRepository.existsByIncidentIdAndCurrentStatusIn(
                    incident.getId(), CollectionStatusType.ACTIVE_STATUSES)).thenReturn(true);

            assertThatThrownBy(() -> service.closeIncident(incident.getId(), author))
                    .isInstanceOf(BusinessRuleException.class)
                    .hasMessage("Incident has an active collection and will be closed when it is completed");

            verifyNoInteractions(incidentProcedure);
        }

        @Test
        @DisplayName("resíduo perigoso sem coleta concluída lança regra de negócio")
        void closeIncident_hazardousWithoutCompletedCollection_throwsBusinessRule() {
            Incident incident = givenOwnedIncident(wasteType(WasteCategory.PERIGOSO, RiskLevel.HIGH), IncidentStatus.PENDING);
            when(collectionRepository.existsByIncidentIdAndCurrentStatus(
                    incident.getId(), CollectionStatusType.COMPLETED.name())).thenReturn(false);

            assertThatThrownBy(() -> service.closeIncident(incident.getId(), author))
                    .isInstanceOf(BusinessRuleException.class)
                    .hasMessage("Hazardous waste incidents can only be closed after a completed collection");

            verifyNoInteractions(incidentProcedure);
        }

        @Test
        @DisplayName("resíduo perigoso com coleta concluída fecha pela procedure")
        void closeIncident_hazardousWithCompletedCollection_closesThroughProcedure() {
            Incident incident = givenOwnedIncident(wasteType(WasteCategory.PERIGOSO, RiskLevel.HIGH), IncidentStatus.PENDING);
            when(collectionRepository.existsByIncidentIdAndCurrentStatus(
                    incident.getId(), CollectionStatusType.COMPLETED.name())).thenReturn(true);

            service.closeIncident(incident.getId(), author);

            verify(incidentProcedure).closeIncident(incident.getId());
        }

        @Test
        @DisplayName("resíduo comum sem coleta ativa fecha pela procedure")
        void closeIncident_commonWasteWithoutActiveCollection_closesThroughProcedure() {
            Incident incident = givenOwnedIncident(wasteType(WasteCategory.PLASTICO, RiskLevel.LOW), IncidentStatus.PENDING);

            service.closeIncident(incident.getId(), author);

            verify(incidentProcedure).closeIncident(incident.getId());
        }
    }
}
