package com.volta.api.service;

import com.volta.api.database.entity.Company;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.dto.company.CompanyRequestDTO;
import com.volta.api.dto.company.CompanyResponseDTO;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.CompanyMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompanyService")
class CompanyServiceTest {

    private static final String CNPJ = "11.222.333/0001-81";
    private static final String NORMALIZED_CNPJ = "11222333000181";

    @Mock
    private CompanyRepository companyRepository;

    private CompanyService service;

    @BeforeEach
    void setUp() {
        service = new CompanyService(companyRepository, new CompanyMapper());
    }

    private CompanyRequestDTO request() {
        return new CompanyRequestDTO("Volta Industry", CNPJ, "Paulista Avenue, 1000");
    }

    private Company company(UUID id, String name, String cnpj) {
        Company company = new Company();
        company.setId(id);
        company.setName(name);
        company.setCnpj(cnpj);
        company.setAddress("Old address");
        return company;
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("CNPJ já cadastrado lança conflito e não salva")
        void register_cnpjAlreadyRegistered_throwsConflictAndDoesNotSave() {
            when(companyRepository.existsByCnpj(NORMALIZED_CNPJ)).thenReturn(true);

            assertThatThrownBy(() -> service.register(request()))
                    .isInstanceOf(ConflictException.class)
                    .hasMessage("CNPJ already registered");

            verify(companyRepository, never()).save(any());
        }

        @Test
        @DisplayName("dados válidos salvam a empresa com o CNPJ normalizado")
        void register_validData_savesCompanyWithNormalizedCnpj() {
            UUID savedId = UUID.randomUUID();
            when(companyRepository.existsByCnpj(NORMALIZED_CNPJ)).thenReturn(false);
            when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> {
                Company saved = invocation.getArgument(0);
                saved.setId(savedId);
                return saved;
            });

            CompanyResponseDTO response = service.register(request());

            ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
            verify(companyRepository).save(captor.capture());
            assertThat(captor.getValue().getCnpj()).isEqualTo(NORMALIZED_CNPJ);
            assertThat(response.id()).isEqualTo(savedId);
            assertThat(response.name()).isEqualTo("Volta Industry");
            assertThat(response.cnpj()).isEqualTo(NORMALIZED_CNPJ);
        }
    }

    @Nested
    @DisplayName("consultas")
    class Queries {

        @Test
        @DisplayName("getCompanies converte todas as empresas")
        void getCompanies_existingRecords_convertsAll() {
            when(companyRepository.findAll()).thenReturn(List.of(
                    company(UUID.randomUUID(), "First", NORMALIZED_CNPJ),
                    company(UUID.randomUUID(), "Second", "11444777000161")));

            assertThat(service.getCompanies()).extracting(CompanyResponseDTO::name)
                    .containsExactly("First", "Second");
        }

        @Test
        @DisplayName("getCompanyById inexistente lança not found")
        void getCompanyById_missing_throwsNotFound() {
            UUID id = UUID.randomUUID();
            when(companyRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getCompanyById(id))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Company not found");
        }

        @Test
        @DisplayName("getCompanyById existente devolve a empresa convertida")
        void getCompanyById_existing_returnsConvertedCompany() {
            Company company = company(UUID.randomUUID(), "First", NORMALIZED_CNPJ);
            when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));

            CompanyResponseDTO response = service.getCompanyById(company.getId());

            assertThat(response.id()).isEqualTo(company.getId());
            assertThat(response.name()).isEqualTo("First");
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("inexistente lança not found")
        void update_missing_throwsNotFound() {
            UUID id = UUID.randomUUID();
            when(companyRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.update(id, request()))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(companyRepository, never()).save(any());
        }

        @Test
        @DisplayName("CNPJ de outra empresa lança conflito")
        void update_cnpjOfAnotherCompany_throwsConflict() {
            Company company = company(UUID.randomUUID(), "First", "11444777000161");
            when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
            when(companyRepository.existsByCnpjAndIdNot(NORMALIZED_CNPJ, company.getId())).thenReturn(true);

            assertThatThrownBy(() -> service.update(company.getId(), request()))
                    .isInstanceOf(ConflictException.class)
                    .hasMessage("CNPJ already registered");

            verify(companyRepository, never()).save(any());
        }

        @Test
        @DisplayName("dados válidos alteram os campos e salvam")
        void update_validData_changesFieldsAndSaves() {
            Company company = company(UUID.randomUUID(), "First", "11444777000161");
            when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
            when(companyRepository.existsByCnpjAndIdNot(NORMALIZED_CNPJ, company.getId())).thenReturn(false);

            CompanyResponseDTO response = service.update(company.getId(), request());

            assertThat(company.getName()).isEqualTo("Volta Industry");
            assertThat(company.getCnpj()).isEqualTo(NORMALIZED_CNPJ);
            assertThat(company.getAddress()).isEqualTo("Paulista Avenue, 1000");
            assertThat(response.cnpj()).isEqualTo(NORMALIZED_CNPJ);
            verify(companyRepository).save(company);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("inexistente lança not found e não apaga")
        void delete_missing_throwsNotFoundAndDoesNotDelete() {
            UUID id = UUID.randomUUID();
            when(companyRepository.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> service.delete(id)).isInstanceOf(ResourceNotFoundException.class);

            verify(companyRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("existente remove pelo id")
        void delete_existing_removesById() {
            UUID id = UUID.randomUUID();
            when(companyRepository.existsById(id)).thenReturn(true);

            service.delete(id);

            verify(companyRepository).deleteById(id);
        }
    }
}
