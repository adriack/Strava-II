package com.strava.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.strava.entity.enumeration.AuthProvider;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public class UserDTO {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Date of Birth is required.")
    @Past(message = "Date of Birth must be in the past.")
    private LocalDate dateOfBirth;

    @Positive(message = "Weight must be greater than zero.")
    private Double weight;

    @Positive(message = "Height must be greater than zero.")
    private Double height;

    @Positive(message = "MaxHeartRate must be greater than zero.")
    private Integer maxHeartRate;

    @Positive(message = "RestingHeartRate must be greater than zero.")
    private Integer restingHeartRate;

    @NotNull(message = "AuthProvider is required.")
    private AuthProvider authProvider;

    @JsonCreator
    public UserDTO(
            @JsonProperty("email") String email,
            @JsonProperty("name") String name,
            @JsonProperty("dateOfBirth") LocalDate dateOfBirth,
            @JsonProperty("authProvider") AuthProvider authProvider,
            @JsonProperty("password") String password) {

        this.email = email;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.authProvider = authProvider;
        this.password = password;
    }

    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public Integer getMaxHeartRate() { return maxHeartRate; }
    public void setMaxHeartRate(Integer maxHeartRate) { this.maxHeartRate = maxHeartRate; }

    public Integer getRestingHeartRate() { return restingHeartRate; }
    public void setRestingHeartRate(Integer restingHeartRate) { this.restingHeartRate = restingHeartRate; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public AuthProvider getAuthProvider() { return authProvider; }
    public void setAuthProvider(AuthProvider authProvider) { this.authProvider = authProvider; }
}
