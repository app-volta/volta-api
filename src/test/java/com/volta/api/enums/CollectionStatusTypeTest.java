package com.volta.api.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CollectionStatusType")
class CollectionStatusTypeTest {

    @ParameterizedTest(name = "{0} -> {1} é permitida")
    @CsvSource({
            "REQUESTED, SCHEDULED",
            "REQUESTED, CANCELED",
            "SCHEDULED, SCHEDULED",
            "SCHEDULED, IN_PROGRESS",
            "SCHEDULED, CANCELED",
            "IN_PROGRESS, COMPLETED"
    })
    @DisplayName("transições permitidas")
    void canTransitionTo_allowedTransition_returnsTrue(CollectionStatusType from, CollectionStatusType to) {
        assertThat(from.canTransitionTo(to)).isTrue();
    }

    @ParameterizedTest(name = "{0} -> {1} é proibida")
    @CsvSource({
            "REQUESTED, REQUESTED",
            "REQUESTED, IN_PROGRESS",
            "REQUESTED, COMPLETED",
            "SCHEDULED, REQUESTED",
            "SCHEDULED, COMPLETED",
            "IN_PROGRESS, REQUESTED",
            "IN_PROGRESS, SCHEDULED",
            "IN_PROGRESS, IN_PROGRESS",
            "IN_PROGRESS, CANCELED"
    })
    @DisplayName("transições proibidas")
    void canTransitionTo_forbiddenTransition_returnsFalse(CollectionStatusType from, CollectionStatusType to) {
        assertThat(from.canTransitionTo(to)).isFalse();
    }

    @ParameterizedTest(name = "COMPLETED -> {0} é proibida")
    @EnumSource(CollectionStatusType.class)
    @DisplayName("COMPLETED é estado final")
    void canTransitionTo_fromCompleted_alwaysReturnsFalse(CollectionStatusType to) {
        assertThat(CollectionStatusType.COMPLETED.canTransitionTo(to)).isFalse();
    }

    @ParameterizedTest(name = "CANCELED -> {0} é proibida")
    @EnumSource(CollectionStatusType.class)
    @DisplayName("CANCELED é estado final")
    void canTransitionTo_fromCanceled_alwaysReturnsFalse(CollectionStatusType to) {
        assertThat(CollectionStatusType.CANCELED.canTransitionTo(to)).isFalse();
    }

    @Test
    @DisplayName("status ativos são REQUESTED, SCHEDULED e IN_PROGRESS")
    void activeStatuses_containsOnlyOpenStatuses() {
        assertThat(CollectionStatusType.ACTIVE_STATUSES)
                .containsExactly("REQUESTED", "SCHEDULED", "IN_PROGRESS");
    }
}
