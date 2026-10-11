package com.volta.api.enums;

import java.util.List;

public enum CollectionStatusType {
    REQUESTED,
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;

    public static final List<String> ACTIVE_STATUSES = List.of(
            REQUESTED.name(),
            SCHEDULED.name(),
            IN_PROGRESS.name()
    );

    public boolean canTransitionTo(CollectionStatusType next) {
        return switch (this) {
            case REQUESTED -> next == SCHEDULED || next == CANCELED;
            case SCHEDULED -> next == SCHEDULED || next == IN_PROGRESS || next == CANCELED;
            case IN_PROGRESS -> next == COMPLETED;
            case COMPLETED, CANCELED -> false;
        };
    }
}
