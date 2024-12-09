package com.google.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.dao.GoogleUserRepository;
import com.google.entity.GoogleUser;

@RestController
@RequestMapping("/api/google")
public class GoogleAuthController {

    private final GoogleUserRepository userRepository;

    public GoogleAuthController(GoogleUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, Boolean>> verifyEmail(@RequestParam String email) {
        boolean registered = userRepository.findByEmail(email).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("registered", registered);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateEmailAndPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<GoogleUser> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return ResponseEntity.ok(Collections.singletonMap("valid", true));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(Collections.singletonMap("error", "Invalid email or password"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        // Verifica si ya existe un usuario con el mismo correo
        Optional<GoogleUser> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Collections.singletonMap("error", "Email already registered"));
        }

        // Si no existe, crear un nuevo usuario
        GoogleUser newUser = new GoogleUser();
        newUser.setEmail(email);
        newUser.setPassword(password);

        // Guardar el usuario en la base de datos
        userRepository.save(newUser);

        // Responder con un mensaje de éxito
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(Collections.singletonMap("message", "User registered successfully"));
    }
}
