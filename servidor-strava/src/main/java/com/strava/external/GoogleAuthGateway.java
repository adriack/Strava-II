package com.strava.external;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class GoogleAuthGateway implements AuthGateway {
	
	private static final int PORT = 8080;
	private static final String API_URL = "http://localhost:" + PORT + "/google/users";
    private final HttpClient httpClient;

    public GoogleAuthGateway() {
    	        this.httpClient = HttpClient.newHttpClient();
    	   
    }
	@Override
	public Optional<Boolean> validateEmail(String email) {
        String url = API_URL + "?email=" + email;
        // Create a request
        try {
            // Create the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            // Send the request and obtain the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        	// If response is OK, parse the response body
        	if (response.statusCode() == 200) {
					return Optional.of(true);
			} else {
				return Optional.of(false);
			}
        } catch (Exception ex) {
        	return Optional.empty();
        }
	}
	@Override
	public Optional<Boolean> validatePassword(String email, String password) {
		
		String url = API_URL + "?email=" + email + "&password=" + password;
        // Create a request
        try {
            // Create the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            // Send the request and obtain the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        	// If response is OK, parse the response body
        	if (response.statusCode() == 200) {
                    return Optional.of(true);
            } else {
                return Optional.of(false);
            }
        } catch (Exception ex) {
        	return Optional.empty();
        }  
	}

	
}