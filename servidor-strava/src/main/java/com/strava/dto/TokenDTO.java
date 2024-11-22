package com.strava.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenDTO {
    private String token;

    @JsonCreator
    public TokenDTO(@JsonProperty("token") String token) {

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is required.");
        }

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
