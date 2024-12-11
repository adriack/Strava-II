package com.strava.dto;

import java.time.LocalDate;

import com.strava.entity.enumeration.SportType;

import jakarta.validation.constraints.AssertTrue;

public class ChallengeFilterDTO {
    private LocalDate startDate = null;
    private LocalDate endDate = null;
    private SportType sport = null;
    private Integer limit = null;

    // Getters y Setters
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public SportType getSport() {
        return sport;
    }

    public void setSport(SportType sport) {
        this.sport = sport;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    // Validación personalizada para asegurarse de que endDate sea posterior o igual a startDate
    @AssertTrue(message = "End date must be greater than or equal to start date.")
    public boolean isValidDateRange() {
        if (startDate != null && endDate != null) {
            return !endDate.isBefore(startDate);
        }
        return true;  // Si no se proporcionan fechas, la validación es válida
    }
}
