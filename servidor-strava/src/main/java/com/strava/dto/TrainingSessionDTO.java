package com.strava.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.strava.entity.TrainingSession;
import com.strava.entity.enumeration.SportType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class TrainingSessionDTO {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotNull(message = "Sport is required.")
    private SportType sport;

    @NotNull(message = "Distance is required.")
    @Positive(message = "Distance must be greater than zero.")
    private Double distance;

    @NotNull(message = "Start date is required.")
    @PastOrPresent(message = "Start date cannot be in the future.")
    private LocalDate startDate;

    @NotNull(message = "Start time is required.")
    private LocalTime startTime;

    @NotNull(message = "Duration is required.")
    @Positive(message = "Duration must be greater than zero.")
    private Double duration;

    @JsonCreator
    public TrainingSessionDTO(
            @JsonProperty("title") String title,
            @JsonProperty("sport") SportType sport,
            @JsonProperty("distance") Double distance,
            @JsonProperty("startDate") LocalDate startDate,
            @JsonProperty("startTime") LocalTime startTime,
            @JsonProperty("duration") Double duration) {

        this.title = title;
        this.sport = sport;
        this.distance = distance;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
    }

    public TrainingSessionDTO(TrainingSession session) {
        this.title = session.getTitle();
        this.sport = session.getSport();
        this.distance = session.getDistance();
        this.startDate = session.getStartDate();
        this.startTime = session.getStartTime();
        this.duration = session.getDuration();
    }

    // Getters y Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public SportType getSport() { return sport; }
    public void setSport(SportType sport) { this.sport = sport; }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public Double getDuration() { return duration; }
    public void setDuration(Double duration) { this.duration = duration; }
}
