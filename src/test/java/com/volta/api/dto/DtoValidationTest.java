package com.volta.api.dto;

import com.volta.api.dto.request.CollectionRequestDTO;
import com.volta.api.dto.request.CollectionScheduleRequestDTO;
import com.volta.api.dto.request.CollectionStatusRequestDTO;
import com.volta.api.dto.request.LoginRequestDTO;
import com.volta.api.dto.request.WasteTypeRequestDTO;
import com.volta.api.enums.CollectionStatusType;
import com.volta.api.enums.RiskLevel;
import com.volta.api.enums.WasteCategory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.volta.api.support.UserFixtures.userRequest;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Validação dos DTOs de entrada")
class DtoValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void openValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        factory.close();
    }

    private static Set<String> invalidFields(Set<? extends ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());
    }

    @Nested
    @DisplayName("LoginRequestDTO")
    class Login {

        @Test
        @DisplayName("e-mail e senha preenchidos são válidos")
        void login_filledEmailAndPassword_isValid() {
            assertThat(validator.validate(new LoginRequestDTO("ana@volta.com", "password123"))).isEmpty();
        }

        @Test
        @DisplayName("sem e-mail aponta o campo email")
        void login_missingEmail_pointsToEmailField() {
            assertThat(invalidFields(validator.validate(new LoginRequestDTO(null, "password123"))))
                    .containsExactly("email");
        }

        @Test
        @DisplayName("e-mail mal formado aponta o campo email")
        void login_malformedEmail_pointsToEmailField() {
            assertThat(invalidFields(validator.validate(new LoginRequestDTO("not-an-email", "password123"))))
                    .containsExactly("email");
        }

        @Test
        @DisplayName("senha em branco aponta o campo password")
        void login_blankPassword_pointsToPasswordField() {
            assertThat(invalidFields(validator.validate(new LoginRequestDTO("ana@volta.com", "   "))))
                    .containsExactly("password");
        }
    }

    @Nested
    @DisplayName("UserRequestDTO")
    class UserRegistration {

        @Test
        @DisplayName("todos os dados válidos passam")
        void userRequest_allValidData_isValid() {
            assertThat(validator.validate(userRequest(UUID.randomUUID()).build())).isEmpty();
        }

        @Test
        @DisplayName("senha curta demais aponta o campo password")
        void userRequest_shortPassword_pointsToPasswordField() {
            assertThat(invalidFields(validator.validate(
                    userRequest(UUID.randomUUID()).password("1234567").build())))
                    .containsExactly("password");
        }

        @Test
        @DisplayName("sem empresa aponta o campo companyId")
        void userRequest_missingCompany_pointsToCompanyIdField() {
            assertThat(invalidFields(validator.validate(userRequest(null).build())))
                    .containsExactly("companyId");
        }
    }

    @Nested
    @DisplayName("WasteTypeRequestDTO")
    class WasteTypeRegistration {

        @Test
        @DisplayName("categoria e risco preenchidos são válidos")
        void wasteType_filledCategoryAndRisk_isValid() {
            assertThat(validator.validate(
                    new WasteTypeRequestDTO(WasteCategory.PLASTICO, null, RiskLevel.LOW))).isEmpty();
        }

        @Test
        @DisplayName("sem categoria aponta o campo category")
        void wasteType_missingCategory_pointsToCategoryField() {
            assertThat(invalidFields(validator.validate(
                    new WasteTypeRequestDTO(null, "description", RiskLevel.LOW))))
                    .containsExactly("category");
        }

        @Test
        @DisplayName("sem nível de risco aponta o campo defaultRiskLevel")
        void wasteType_missingRiskLevel_pointsToDefaultRiskLevelField() {
            assertThat(invalidFields(validator.validate(
                    new WasteTypeRequestDTO(WasteCategory.PLASTICO, "description", null))))
                    .containsExactly("defaultRiskLevel");
        }
    }

    @Nested
    @DisplayName("DTOs de coleta")
    class CollectionRequests {

        @Test
        @DisplayName("solicitação sem incidente, cooperativa e tipo aponta os três campos")
        void collectionRequest_missingRequiredFields_pointsToAllThreeFields() {
            assertThat(invalidFields(validator.validate(new CollectionRequestDTO(null, null, " ", false))))
                    .containsExactlyInAnyOrder("incidentId", "cooperativeId", "collectionType");
        }

        @Test
        @DisplayName("solicitação completa é válida")
        void collectionRequest_completeData_isValid() {
            assertThat(validator.validate(
                    new CollectionRequestDTO(UUID.randomUUID(), UUID.randomUUID(), "Recyclable", false))).isEmpty();
        }

        @Test
        @DisplayName("atualização de status sem status aponta o campo status")
        void collectionStatus_missingStatus_pointsToStatusField() {
            assertThat(invalidFields(validator.validate(new CollectionStatusRequestDTO(null, "note"))))
                    .containsExactly("status");
        }

        @Test
        @DisplayName("atualização de status com status é válida")
        void collectionStatus_withStatus_isValid() {
            assertThat(validator.validate(
                    new CollectionStatusRequestDTO(CollectionStatusType.IN_PROGRESS, null))).isEmpty();
        }

        @Test
        @DisplayName("agendamento no passado aponta o campo scheduledAt")
        void collectionSchedule_pastDate_pointsToScheduledAtField() {
            assertThat(invalidFields(validator.validate(
                    new CollectionScheduleRequestDTO(LocalDateTime.now().minusDays(1)))))
                    .containsExactly("scheduledAt");
        }

        @Test
        @DisplayName("agendamento no futuro é válido")
        void collectionSchedule_futureDate_isValid() {
            assertThat(validator.validate(
                    new CollectionScheduleRequestDTO(LocalDateTime.now().plusDays(1)))).isEmpty();
        }
    }
}
