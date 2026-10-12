package com.volta.api.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CnpjUtils")
class CnpjUtilsTest {

    @ParameterizedTest(name = "\"{0}\" vira {1}")
    @CsvSource({
            "11.222.333/0001-81, 11222333000181",
            "11222333000181, 11222333000181",
            "12.abc.345/01de-35, 12ABC34501DE35",
            "'  11 222 333 0001 81 ', 11222333000181"
    })
    @DisplayName("remove a máscara e deixa em maiúsculas")
    void normalize_maskedValue_returnsUnmaskedUppercase(String cnpj, String expected) {
        assertThat(CnpjUtils.normalize(cnpj)).isEqualTo(expected);
    }

    @Test
    @DisplayName("nulo continua nulo")
    void normalize_null_returnsNull() {
        assertThat(CnpjUtils.normalize(null)).isNull();
    }
}
