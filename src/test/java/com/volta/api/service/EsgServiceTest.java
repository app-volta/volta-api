package com.volta.api.service;

import com.volta.api.database.entity.Company;
import com.volta.api.database.entity.EsgMetric;
import com.volta.api.database.function.EsgFunction;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.database.repository.EsgMetricRepository;
import com.volta.api.dto.esg.EsgMetricRequestDTO;
import com.volta.api.dto.esg.EsgMetricResponseDTO;
import com.volta.api.dto.esg.EsgScoreResponseDTO;
import com.volta.api.dto.esg.RecyclingPercentageRequestDTO;
import com.volta.api.dto.esg.RecyclingPercentageResponseDTO;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.EsgMetricMapper;
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
@DisplayName("EsgService")
class EsgServiceTest {

    @Mock
    private EsgFunction esgFunction;

    @Mock
    private EsgMetricRepository esgMetricRepository;

    @Mock
    private CompanyRepository companyRepository;

    private EsgService service;

    private final UUID companyId = UUID.randomUUID();

    private final AuthenticatedUser author = authenticatedUser(UUID.randomUUID(), companyId, "MANAGER");

    @BeforeEach
    void setUp() {
        service = new EsgService(esgFunction, esgMetricRepository, companyRepository, new EsgMetricMapper());
    }

    @Nested
    @DisplayName("calculateRecyclingPercentage")
    class CalculateRecyclingPercentage {

        @Test
        @DisplayName("reciclado maior que o total lança regra de negócio sem chamar a function")
        void calculate_recycledGreaterThanWaste_throwsBusinessRule() {
            RecyclingPercentageRequestDTO request =
                    new RecyclingPercentageRequestDTO(new BigDecimal("100"), new BigDecimal("100.01"));

            assertThatThrownBy(() -> service.calculateRecyclingPercentage(request))
                    .isInstanceOf(BusinessRuleException.class)
                    .hasMessage("Total recycled cannot be greater than total waste");

            verifyNoInteractions(esgFunction);
        }

        @Test
        @DisplayName("totais válidos devolvem o percentual calculado pelo banco")
        void calculate_validTotals_returnsPercentageFromDatabaseFunction() {
            BigDecimal waste = new BigDecimal("1000");
            BigDecimal recycled = new BigDecimal("750");
            when(esgFunction.calculateRecyclingPercentage(waste, recycled)).thenReturn(new BigDecimal("75.00"));

            RecyclingPercentageResponseDTO response =
                    service.calculateRecyclingPercentage(new RecyclingPercentageRequestDTO(waste, recycled));

            assertThat(response.totalWasteKg()).isEqualByComparingTo("1000");
            assertThat(response.totalRecycledKg()).isEqualByComparingTo("750");
            assertThat(response.recyclingPercentage()).isEqualByComparingTo("75.00");
        }

        @Test
        @DisplayName("reciclado igual ao total é aceito")
        void calculate_recycledEqualToWaste_isAccepted() {
            BigDecimal total = new BigDecimal("500");
            when(esgFunction.calculateRecyclingPercentage(total, total)).thenReturn(new BigDecimal("100.00"));

            RecyclingPercentageResponseDTO response =
                    service.calculateRecyclingPercentage(new RecyclingPercentageRequestDTO(total, total));

            assertThat(response.recyclingPercentage()).isEqualByComparingTo("100.00");
        }
    }

    @Nested
    @DisplayName("getCompanyScore")
    class GetCompanyScore {

        @Test
        @DisplayName("calcula o score da empresa do autor")
        void getCompanyScore_returnsScoreOfAuthorCompany() {
            when(esgFunction.calculateCompanyEsgScore(companyId)).thenReturn(new BigDecimal("82.75"));

            EsgScoreResponseDTO response = service.getCompanyScore(author);

            assertThat(response.companyId()).isEqualTo(companyId);
            assertThat(response.score()).isEqualByComparingTo("82.75");
        }
    }

    @Nested
    @DisplayName("registerMetric")
    class RegisterMetric {

        private final EsgMetricRequestDTO request =
                new EsgMetricRequestDTO("2026-09", new BigDecimal("1500.00"), new BigDecimal("1200.00"));

        @Test
        @DisplayName("reciclado maior que o total lança regra de negócio sem consultar o banco")
        void registerMetric_recycledGreaterThanWaste_throwsBusinessRule() {
            EsgMetricRequestDTO invalid =
                    new EsgMetricRequestDTO("2026-09", new BigDecimal("10.00"), new BigDecimal("20.00"));

            assertThatThrownBy(() -> service.registerMetric(invalid, author))
                    .isInstanceOf(BusinessRuleException.class);

            verifyNoInteractions(esgMetricRepository, companyRepository, esgFunction);
        }

        @Test
        @DisplayName("período já registrado lança conflito")
        void registerMetric_periodAlreadyRegistered_throwsConflict() {
            when(esgMetricRepository.existsByCompanyIdAndPeriod(companyId, "2026-09")).thenReturn(true);

            assertThatThrownBy(() -> service.registerMetric(request, author))
                    .isInstanceOf(ConflictException.class)
                    .hasMessage("ESG metric already registered for this period");

            verify(esgMetricRepository, never()).saveAndFlush(any());
        }

        @Test
        @DisplayName("empresa inexistente lança not found")
        void registerMetric_missingCompany_throwsNotFound() {
            when(esgMetricRepository.existsByCompanyIdAndPeriod(companyId, "2026-09")).thenReturn(false);
            when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.registerMetric(request, author))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Company not found");
        }

        @Test
        @DisplayName("dados válidos salvam a métrica com o percentual calculado pelo banco")
        void registerMetric_validData_savesMetricWithDatabasePercentage() {
            Company company = company(companyId);
            when(esgMetricRepository.existsByCompanyIdAndPeriod(companyId, "2026-09")).thenReturn(false);
            when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
            when(esgFunction.calculateRecyclingPercentage(request.totalWasteKg(), request.totalRecycledKg()))
                    .thenReturn(new BigDecimal("80.00"));
            when(esgMetricRepository.saveAndFlush(any(EsgMetric.class))).thenAnswer(invocation -> invocation.getArgument(0));

            EsgMetricResponseDTO response = service.registerMetric(request, author);

            ArgumentCaptor<EsgMetric> captor = ArgumentCaptor.forClass(EsgMetric.class);
            verify(esgMetricRepository).saveAndFlush(captor.capture());
            assertThat(captor.getValue().getCompany()).isSameAs(company);
            assertThat(captor.getValue().getPeriod()).isEqualTo("2026-09");
            assertThat(response.companyId()).isEqualTo(companyId);
            assertThat(response.recyclingPercentage()).isEqualByComparingTo("80.00");
            assertThat(response.totalWasteKg()).isEqualByComparingTo("1500.00");
            assertThat(response.totalRecycledKg()).isEqualByComparingTo("1200.00");
        }
    }

    @Nested
    @DisplayName("getMetrics")
    class GetMetrics {

        @Test
        @DisplayName("lista as métricas da empresa do autor")
        void getMetrics_returnsMetricsOfAuthorCompany() {
            EsgMetric metric = new EsgMetric();
            metric.setId(UUID.randomUUID());
            metric.setCompany(company(companyId));
            metric.setPeriod("2026-08");
            when(esgMetricRepository.findByCompanyIdOrderByCalculatedAtDesc(companyId)).thenReturn(List.of(metric));

            List<EsgMetricResponseDTO> response = service.getMetrics(author);

            assertThat(response).hasSize(1);
            assertThat(response.get(0).id()).isEqualTo(metric.getId());
            assertThat(response.get(0).period()).isEqualTo("2026-08");
        }

        @Test
        @DisplayName("sem métricas devolve lista vazia")
        void getMetrics_noMetrics_returnsEmptyList() {
            when(esgMetricRepository.findByCompanyIdOrderByCalculatedAtDesc(companyId)).thenReturn(List.of());

            assertThat(service.getMetrics(author)).isEmpty();
        }
    }
}
