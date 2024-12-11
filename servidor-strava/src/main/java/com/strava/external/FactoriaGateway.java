package com.strava.external;

import org.springframework.stereotype.Component;

@Component
public class FactoriaGateway {

    public AuthGateway createGateway(String provider) {
        return switch (provider.toLowerCase()) {
            case "meta" -> new MetaAuthGateway();
            case "google" -> new GoogleAuthGateway();
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }
}
