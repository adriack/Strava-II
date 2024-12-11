package com.strava.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.strava.entity.Challenge;
import com.strava.entity.enumeration.ObjectiveType;
import com.strava.entity.enumeration.SportType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class ChallengeDTO {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Start date is required.")
    @PastOrPresent(message = "Start date cannot be in the future.")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    private LocalDate endDate;

    @NotNull(message = "Objective value must be provided.")
    @Positive(message = "Objective value must be greater than zero.")
    private Double objectiveValue;

    @NotNull(message = "Objective type is required.")
    private ObjectiveType objectiveType;

    @NotNull(message = "Sport type is required.")
    private SportType sport;

    // Validación personalizada para comprobar que endDate es posterior o igual a startDate
    @AssertTrue(message = "End date must be greater than or equal to start date.")
    public boolean isEndDateAfterStartDate() {
        return endDate == null || startDate == null || !endDate.isBefore(startDate);
    }

    @JsonCreator
    public ChallengeDTO(
            @JsonProperty("name") String name,
            @JsonProperty("startDate") LocalDate startDate,
            @JsonProperty("endDate") LocalDate endDate,
            @JsonProperty("objectiveValue") Double objectiveValue,
            @JsonProperty("objectiveType") ObjectiveType objectiveType,
            @JsonProperty("sport") SportType sport) {

        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.objectiveValue = objectiveValue;
        this.objectiveType = objectiveType;
        this.sport = sport;
    }

    public ChallengeDTO(Challenge challenge) {
        this.name = challenge.getName();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
        this.objectiveValue = challenge.getObjectiveValue();
        this.objectiveType = challenge.getObjectiveType();
        this.sport = challenge.getSport();
    }

    // Getters y Setters
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
