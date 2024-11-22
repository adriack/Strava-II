package com.strava.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.strava.entity.Challenge;
import com.strava.entity.ObjectiveType;
import com.strava.entity.SportType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChallengeDTO {
    private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double objectiveValue;
    private ObjectiveType objectiveType;
    private SportType sport;

    @JsonCreator
    public ChallengeDTO(
            @JsonProperty("name") String name,
            @JsonProperty("startDate") LocalDate startDate,
            @JsonProperty("endDate") LocalDate endDate,
            @JsonProperty("objectiveValue") Double objectiveValue,
            @JsonProperty("objectiveType") ObjectiveType objectiveType,
            @JsonProperty("sport") SportType sport) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date is required.");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date is required.");
        }
        if (objectiveValue == null || objectiveValue <= 0) {
            throw new IllegalArgumentException("Objective value must be greater than zero.");
        }
        if (objectiveType == null) {
            throw new IllegalArgumentException("Objective type is required.");
        }
        if (sport == null) {
            throw new IllegalArgumentException("Sport type is required.");
        }

        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.objectiveValue = objectiveValue;
        this.objectiveType = objectiveType;
        this.sport = sport;
    }

    public ChallengeDTO(Challenge challenge) {
        this.id = challenge.getId();
        this.name = challenge.getName();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
        this.objectiveValue = challenge.getObjectiveValue();
        this.objectiveType = challenge.getObjectiveType();
        this.sport = challenge.getSport();
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Double getObjectiveValue() { return objectiveValue; }
    public void setObjectiveValue(Double objectiveValue) { this.objectiveValue = objectiveValue; }

    public ObjectiveType getObjectiveType() { return objectiveType; }
    public void setObjectiveType(ObjectiveType objectiveType) { this.objectiveType = objectiveType; }

    public SportType getSport() { return sport; }
    public void setSport(SportType sport) { this.sport = sport; }
}
