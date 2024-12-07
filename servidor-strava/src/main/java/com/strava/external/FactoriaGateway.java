package com.strava.external;

public class FactoriaGateway {

    public AuthGateway createGateway(String provider) {
        switch (provider.toLowerCase()) {
            case "meta":
                return new MetaAuthGateway();
            case "google":
                return new GoogleAuthGateway();
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }
}