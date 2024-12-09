package com.strava.external;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

public class MetaAuthGateway implements AuthGateway {
    private static final String HOST = "localhost";
    private static final int PORT = 8081;

    @Override
    public Optional<Boolean> validateEmail(String email) {
        return Optional.of(sendRequest("VALIDATE_EMAIL " + email).equals("EMAIL_VALID"));
    }

    @Override
    public Optional<Boolean> validatePassword(String email, String password) {
        return Optional.of(sendRequest("VALIDATE_PASSWORD " + email + " " + password).equals("PASSWORD_VALID"));
    }

    private String sendRequest(String request) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(request);
            return in.readLine(); // Lee la respuesta del servidor
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}