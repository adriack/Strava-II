package com.strava.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.strava.entity.enumeration.AuthProvider;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO for user login information.")
public class LoginDTO {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    @Schema(description = "The user's email address.", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required.")
    @Schema(description = "The user's password.", example = "P@ssw0rd", required = true)
    private String password;

    @NotNull(message = "AuthProvider is required.")
    @Schema(description = "The authentication provider used for login.", example = "GOOGLE", required = true)
    private AuthProvider authProvider;

    @JsonCreator
    public LoginDTO(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("authProvider") AuthProvider authProvider) {
        this.email = email;
        this.password = password;
        this.authProvider = authProvider;
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

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }
}
