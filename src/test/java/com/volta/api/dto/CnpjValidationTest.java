package com.volta.api.dto;

import com.volta.api.dto.company.CompanyRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Validação de CNPJ")
class CnpjValidationTest {

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

    private Set<ConstraintViolation<CompanyRequestDTO>> validate(String cnpj) {
        return validator.validate(new CompanyRequestDTO("Volta Industry", cnpj, "Av. Paulista, 1000"));
    }

    @ParameterizedTest(name = "\"{0}\" é válido")
    @ValueSource(strings = {
            "11222333000181",
            "11.222.333/0001-81",
            "11444777000161",
            "12ABC34501DE35",
            "12abc34501de35",
            "12.ABC.345/01DE-35"
    })
    @DisplayName("aceita CNPJ numérico e alfanumérico, com ou sem máscara")
    void cnpj_validValue_isAccepted(String cnpj) {
        assertThat(validate(cnpj)).isEmpty();
    }

    @ParameterizedTest(name = "\"{0}\" é inválido")
    @ValueSource(strings = {
            "11222333000182",
            "12ABC34501DE36",
            "00000000000000",
            "11111111111111",
            "1122233300018",
            "112223330001811",
            "1122233300018A",
            "ABCDEFGHIJKLMN"
    })
    @DisplayName("rejeita dígito verificador errado, repetidos, tamanho errado e letras nos verificadores")
    void cnpj_invalidValue_isRejected(String cnpj) {
        assertThat(validate(cnpj))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("cnpj");
    }

    @Test
    @DisplayName("mensagem de erro é CNPJ inválido")
    void cnpj_invalidValue_usesExpectedMessage() {
        assertThat(validate("11222333000182"))
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("CNPJ inválido");
    }
}
