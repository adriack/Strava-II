package com.strava.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strava.dao.TokenDAO;
import com.strava.dao.UserDAO;
import com.strava.dto.LoginDTO;
import com.strava.dto.TokenDTO;
import com.strava.dto.UserDTO;
import com.strava.entity.User;
import com.strava.entity.UserToken;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TokenDAO tokenDAO;

    // Registrar un nuevo usuario
    public String registerUser(UserDTO userDTO) {
        // Verificar si ya existe un usuario con el mismo correo
        if (userDAO.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email already exists.");
        }

        // Crear un nuevo usuario
        User user = new User(userDTO);
        user.setId(UUID.randomUUID());

        // Guardar el usuario en la base de datos
        userDAO.save(user);

        return "User registered successfully with ID: " + user.getId();
    }

    // Iniciar sesión de usuario
    public String loginUser(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        // Buscar al usuario por su email
        Optional<User> optionalUser = userDAO.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        User user = optionalUser.get();

        // Validar las credenciales
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        // Generar y guardar el token
        String token = generateToken();
        UserToken userToken = new UserToken(user, token);
        tokenDAO.save(userToken);

        return token;
    }

    // Cerrar sesión de usuario
    public String logoutUser(TokenDTO tokenDTO) {
        String token = tokenDTO.getToken();

        // Buscar el token en la base de datos
        Optional<UserToken> optionalToken = tokenDAO.findByToken(token);

        if (optionalToken.isEmpty()) {
            throw new IllegalArgumentException("Invalid token.");
        }

        // Invalidar el token
        UserToken userToken = optionalToken.get();
        userToken.setRevoked(true);
        tokenDAO.save(userToken);

        return "User logged out successfully.";
    }

    // Validar el token y obtener el usuario asociado
    public User getUserFromToken(TokenDTO tokenDTO) {
        String token = tokenDTO.getToken();
    
        // Buscar al usuario asociado al token no revocado directamente desde el DAO
        return tokenDAO.findUserByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or revoked token."));
    }    

    // Método para generar un token único basado en UUID
    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
