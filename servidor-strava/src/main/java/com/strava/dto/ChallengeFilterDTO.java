package com.strava.dto;

import com.strava.entity.SportType;
import java.time.LocalDate;

public class ChallengeFilterDTO {
    private LocalDate date = LocalDate.now();
    private SportType sport = null;
    private Integer limit = 5;

    // Getters y Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
