package com.strava.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.strava.entity.SportType;
import com.strava.entity.TrainingSession;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainingSessionDTO {
    private UUID id;
    private String title;
    private SportType sport;
    private Double distance;
    private LocalDate startDate;
    private LocalTime startTime;
    private Double duration;

    @JsonCreator
    public TrainingSessionDTO(
            @JsonProperty("title") String title,
            @JsonProperty("sport") SportType sport,
            @JsonProperty("distance") Double distance,
            @JsonProperty("startDate") LocalDate startDate,
            @JsonProperty("startTime") LocalTime startTime,
            @JsonProperty("duration") Double duration) {

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (sport == null) {
            throw new IllegalArgumentException("Sport is required.");
        }
        if (distance == null || distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than zero.");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date is required.");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Start time is required.");
        }
        if (duration == null || duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero.");
        }

        this.title = title;
        this.sport = sport;
        this.distance = distance;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
    }
    
    public TrainingSessionDTO(TrainingSession session) {
        this.id = session.getId();
        this.title = session.getTitle();
        this.sport = session.getSport();
        this.distance = session.getDistance();
        this.startDate = session.getStartDate();
        this.startTime = session.getStartTime();
        this.duration = session.getDuration();
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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
