package com.volta.api.enums;

public enum RiskLevel {
    LOW,
    MEDIUM,
    HIGH;

    public static boolean isHazardous(String riskLevel) {
        return HIGH.name().equals(riskLevel);
    }
}
