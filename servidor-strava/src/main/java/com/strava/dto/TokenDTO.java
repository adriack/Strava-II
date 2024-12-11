package com.strava.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class TokenDTO {
    
    @NotBlank(message = "Token is required.")
    private String token;

    @JsonCreator
    public TokenDTO(@JsonProperty("token") String token) {
        this.token = token;
    }

    // Getter y Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
