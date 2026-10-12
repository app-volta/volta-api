package com.volta.api.service;

import com.volta.api.database.entity.Area;
import com.volta.api.database.entity.Company;
import com.volta.api.database.repository.AreaRepository;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.dto.area.AreaRequestDTO;
import com.volta.api.dto.area.AreaResponseDTO;
import com.volta.api.dto.area.AreaUpdateRequestDTO;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.AreaMapper;
import com.volta.api.security.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.volta.api.support.UserFixtures.authenticatedUser;
import static com.volta.api.support.UserFixtures.company;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AreaService")
class AreaServiceTest {

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private CompanyRepository companyRepository;

    private AreaService service;

    private final UUID companyId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new AreaService(areaRepository, companyRepository, new AreaMapper());
    }

    private Area area(UUID id, Company company, String sectorName) {
        Area area = new Area();
        area.setId(id);
        area.setCompany(company);
        area.setSectorName(sectorName);
        area.setLocationDescription("Block B");
        return area;
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("empresa inexistente lança not found e não salva")
        void register_missingCompany_throwsNotFoundAndDoesNotSave() {
            when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.register(new AreaRequestDTO(companyId, "Warehouse", "Block B")))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Company not found");

            verify(areaRepository, never()).save(any());
        }

        @Test
        @DisplayName("dados válidos salvam a área vinculada à empresa")
        void register_validData_savesAreaLinkedToCompany() {
            Company company = company(companyId);
            UUID savedId = UUID.randomUUID();
            when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
            when(areaRepository.save(any(Area.class))).thenAnswer(invocation -> {
                Area saved = invocation.getArgument(0);
                saved.setId(savedId);
                return saved;
            });

            AreaResponseDTO response = service.register(new AreaRequestDTO(companyId, "Warehouse", "Block B"));

            ArgumentCaptor<Area> captor = ArgumentCaptor.forClass(Area.class);
            verify(areaRepository).save(captor.capture());
            assertThat(captor.getValue().getCompany()).isSameAs(company);
            assertThat(response.id()).isEqualTo(savedId);
            assertThat(response.companyId()).isEqualTo(companyId);
            assertThat(response.sectorName()).isEqualTo("Warehouse");
            assertThat(response.locationDescription()).isEqualTo("Block B");
        }
    }

    @Nested
    @DisplayName("consultas")
    class Queries {

        @Test
        @DisplayName("getAreas converte todas as áreas")
        void getAreas_existingAreas_convertsAll() {
            Company company = company(companyId);
            when(areaRepository.findAll()).thenReturn(List.of(
                    area(UUID.randomUUID(), company, "Warehouse"),
                    area(UUID.randomUUID(), company, "Kitchen")));

            assertThat(service.getAreas()).extracting(AreaResponseDTO::sectorName)
                    .containsExactly("Warehouse", "Kitchen");
        }

        @Test
        @DisplayName("getAreaById inexistente lança not found")
        void getAreaById_missing_throwsNotFound() {
            UUID id = UUID.randomUUID();
            when(areaRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getAreaById(id))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Area not found");
        }

        @Test
        @DisplayName("getAreaById existente devolve a área convertida")
        void getAreaById_existing_returnsConvertedArea() {
            Area area = area(UUID.randomUUID(), company(companyId), "Warehouse");
            when(areaRepository.findById(area.getId())).thenReturn(Optional.of(area));

            AreaResponseDTO response = service.getAreaById(area.getId());

            assertThat(response.id()).isEqualTo(area.getId());
            assertThat(response.sectorName()).isEqualTo("Warehouse");
        }

        @Test
        @DisplayName("getAreasOfMyCompany busca somente pela empresa do autor")
        void getAreasOfMyCompany_filtersByAuthorCompany() {
            AuthenticatedUser author = authenticatedUser(UUID.randomUUID(), companyId, "EMPLOYEE");
            when(areaRepository.findByCompanyId(companyId)).thenReturn(List.of(
                    area(UUID.randomUUID(), company(companyId), "Warehouse")));

            List<AreaResponseDTO> response = service.getAreasOfMyCompany(author);

            assertThat(response).hasSize(1);
            assertThat(response.get(0).companyId()).isEqualTo(companyId);
            verify(areaRepository).findByCompanyId(companyId);
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("inexistente lança not found e não salva")
        void update_missing_throwsNotFoundAndDoesNotSave() {
            UUID id = UUID.randomUUID();
            when(areaRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.update(id, new AreaUpdateRequestDTO("Kitchen", "Block C")))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(areaRepository, never()).save(any());
        }

        @Test
        @DisplayName("existente altera os campos e salva")
        void update_existing_changesFieldsAndSaves() {
            Area area = area(UUID.randomUUID(), company(companyId), "Warehouse");
            when(areaRepository.findById(area.getId())).thenReturn(Optional.of(area));

            AreaResponseDTO response = service.update(area.getId(), new AreaUpdateRequestDTO("Kitchen", "Block C"));

            assertThat(area.getSectorName()).isEqualTo("Kitchen");
            assertThat(area.getLocationDescription()).isEqualTo("Block C");
            assertThat(response.sectorName()).isEqualTo("Kitchen");
            verify(areaRepository).save(area);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("inexistente lança not found e não apaga")
        void delete_missing_throwsNotFoundAndDoesNotDelete() {
            UUID id = UUID.randomUUID();
            when(areaRepository.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> service.delete(id)).isInstanceOf(ResourceNotFoundException.class);

            verify(areaRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("existente remove pelo id")
        void delete_existing_removesById() {
            UUID id = UUID.randomUUID();
            when(areaRepository.existsById(id)).thenReturn(true);

            service.delete(id);

            verify(areaRepository).deleteById(id);
        }
    }
}
