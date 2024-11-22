package com.strava.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.strava.dto.ChallengeDTO;

public class Challenge {
    private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double objectiveValue;
    private ObjectiveType objectiveType;
    private SportType sport;

    private List<UUID> userIds = new ArrayList<>();

    public Challenge(ChallengeDTO challengeDTO) {
        this.id = challengeDTO.getId();
        this.name = challengeDTO.getName();
        this.startDate = challengeDTO.getStartDate();
        this.endDate = challengeDTO.getEndDate();
        this.objectiveValue = challengeDTO.getObjectiveValue();
        this.objectiveType = challengeDTO.getObjectiveType();
        this.sport = challengeDTO.getSport();
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

    // Métodos para acceder y modificar userIds
    public List<UUID> getUserIds() { return new ArrayList<>(userIds); }
    
    public void addUserId(UUID userId) { 
        if (userId != null && !userIds.contains(userId)) {
            userIds.add(userId); 
        }
    }
    
    public void removeUserId(UUID userId) { userIds.remove(userId); }
}


