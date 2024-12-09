package com.google.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDTO {
    private String email;
    private String password;

    @JsonCreator
    public LoginDTO(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password) {

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        this.email = email;
        this.password = password;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
