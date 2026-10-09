package com.volta.api.enums;

import java.time.Duration;

public enum Priority {
    LOW(Duration.ofDays(15)),
    MEDIUM(Duration.ofDays(7)),
    HIGH(Duration.ofHours(72)),
    CRITICAL(Duration.ofHours(24));

    public static final Duration URGENT_SCHEDULING_DEADLINE = Duration.ofHours(24);

    private final Duration schedulingDeadline;

    Priority(Duration schedulingDeadline) {
        this.schedulingDeadline = schedulingDeadline;
    }

    public Duration getSchedulingDeadline() {
        return schedulingDeadline;
    }

    public boolean isLowerThan(Priority other) {
        return this.compareTo(other) < 0;
    }
}
