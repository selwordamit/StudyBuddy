package com.amit.studybuddy.domain.enums;

public enum DegreeType {
    COMPUTER_SCIENCE(3),
    INDUSTRIAL_ENGINEERING(4),
    MATHEMATICS(3);

    private final int durationInYears;

    DegreeType(int years) {
        this.durationInYears = years;
    }

    public int getDurationInYears() {
        return durationInYears;
    }
}
