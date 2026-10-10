package com.volta.api.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Priority")
class PriorityTest {

    @ParameterizedTest(name = "{0} tem prazo de {1} horas")
    @CsvSource({
            "LOW, 360",
            "MEDIUM, 168",
            "HIGH, 72",
            "CRITICAL, 24"
    })
    @DisplayName("prazo de agendamento por prioridade")
    void getSchedulingDeadline_eachPriority_returnsExpectedDuration(Priority priority, long hours) {
        assertThat(priority.getSchedulingDeadline()).isEqualTo(Duration.ofHours(hours));
    }

    @Test
    @DisplayName("prazo de urgência é de 24 horas")
    void urgentSchedulingDeadline_is24Hours() {
        assertThat(Priority.URGENT_SCHEDULING_DEADLINE).isEqualTo(Duration.ofHours(24));
    }

    @ParameterizedTest(name = "{0} é menor que {1}")
    @CsvSource({
            "LOW, MEDIUM",
            "LOW, HIGH",
            "MEDIUM, HIGH",
            "HIGH, CRITICAL"
    })
    @DisplayName("prioridade menor")
    void isLowerThan_lowerPriority_returnsTrue(Priority lower, Priority higher) {
        assertThat(lower.isLowerThan(higher)).isTrue();
    }

    @ParameterizedTest(name = "{0} não é menor que {1}")
    @CsvSource({
            "LOW, LOW",
            "HIGH, HIGH",
            "HIGH, LOW",
            "CRITICAL, MEDIUM"
    })
    @DisplayName("prioridade igual ou maior")
    void isLowerThan_equalOrHigherPriority_returnsFalse(Priority priority, Priority other) {
        assertThat(priority.isLowerThan(other)).isFalse();
    }
}
