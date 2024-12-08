package com.strava.dto;

import java.time.LocalDate;

import com.strava.entity.enumeration.SportType;

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
}
