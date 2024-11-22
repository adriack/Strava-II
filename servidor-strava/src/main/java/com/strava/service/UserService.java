package com.strava.service;

import com.strava.entity.User;
import com.strava.entity.AuthProvider;
import com.strava.dto.UserDTO;
import com.strava.dto.TokenDTO;
import com.strava.dto.LoginDTO;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    // Mapa para almacenar los usuarios con su UUID
    private Map<UUID, User> userDatabase = new HashMap<>();
    // Mapa para almacenar los tokens válidos asociados a los UUID de los usuarios
    private Map<String, UUID> tokenDatabase = new HashMap<>();

    // Método para registrar un nuevo usuario
    public String registerUser(UserDTO userDTO) {
        // Comprobar si ya existe un usuario con el mismo email
        if (findUserByEmail(userDTO.getEmail()) != null) {
            throw new IllegalArgumentException("This email already exists.");
        }

        // Generar un UUID único para el nuevo usuario
        UUID userId = UUID.randomUUID();
        userDTO.setId(userId);
        
        // Crear un nuevo objeto User a partir del DTO
        User user = new User(userDTO);
        
        // Guardar el usuario en la base de datos (HashMap)
        userDatabase.put(userId, user);
        
        return "User registered successfully with ID: " + userId;
    }

    public String loginUser(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        AuthProvider authProvider = loginDTO.getAuthProvider();
        
        // Verificar las credenciales del usuario
        User user = findUserByEmail(email);
        if (user == null || !user.getPassword().equals(password) || !user.getAuthProvider().equals(authProvider)) {
            throw new IllegalArgumentException("Invalid credentials.");
        }
        
        // Generar un token para el usuario
        String token = generateToken();
        
        // Guardar el token en la base de datos
        tokenDatabase.put(token, user.getId());
        
        return token;
    }

    public String logoutUser(TokenDTO tokenDTO) {
        String token = tokenDTO.getToken();
        
        // Verificar si el token existe y eliminarlo
        UUID userId = tokenDatabase.remove(token);
        
        if (userId == null) {
            throw new IllegalArgumentException("Invalid token.");
        }

        return "User logged out successfully.";
    }

    // Método para generar un token único basado en el timestamp actual
    private String generateToken() {
        long timestamp = System.currentTimeMillis();
        return String.valueOf(timestamp); // Usamos el timestamp como token único
    }

    // Método auxiliar para buscar un usuario por su correo
    private User findUserByEmail(String email) {
        for (User user : userDatabase.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null; // Si no se encuentra el usuario
    }

    // Validar el token y obtener el usuario asociado
    public User getUserFromToken(TokenDTO tokenDTO) {
        String token = tokenDTO.getToken();
        
        // Verificar si el token existe en el tokenDatabase
        UUID userId = tokenDatabase.get(token);
        
        if (userId == null) {
            // Si el token no es válido o no existe
            return null;
        }
        
        // Si el token es válido, devolver el objeto User correspondiente
        return userDatabase.get(userId);
    }
}
