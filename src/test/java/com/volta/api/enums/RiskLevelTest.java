package com.volta.api.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RiskLevel")
class RiskLevelTest {

    @Test
    @DisplayName("HIGH é perigoso")
    void isHazardous_high_returnsTrue() {
        assertThat(RiskLevel.isHazardous("HIGH")).isTrue();
    }

    @ParameterizedTest(name = "\"{0}\" não é perigoso")
    @ValueSource(strings = {"LOW", "MEDIUM", "high", "", "UNKNOWN"})
    @DisplayName("demais valores não são perigosos")
    void isHazardous_otherValues_returnsFalse(String riskLevel) {
        assertThat(RiskLevel.isHazardous(riskLevel)).isFalse();
    }

    @Test
    @DisplayName("nulo não é perigoso")
    void isHazardous_null_returnsFalse() {
        assertThat(RiskLevel.isHazardous(null)).isFalse();
    }
}
