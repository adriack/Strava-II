package com.strava.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strava.dao.TokenDAO;
import com.strava.dao.UserDAO;
import com.strava.dto.LoginDTO;
import com.strava.dto.ResponseWrapper;
import com.strava.dto.TokenDTO;
import com.strava.dto.UserDTO;
import com.strava.entity.User;
import com.strava.entity.UserToken;
import com.strava.external.AuthGateway;
import com.strava.external.FactoriaGateway;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TokenDAO tokenDAO;

    private final FactoriaGateway factoriaGateway;

    public UserService() {
        this.factoriaGateway = new FactoriaGateway();
    }

    // Registrar un nuevo usuario
    public ResponseWrapper<String> registerUser(UserDTO userDTO) {
        if (userDAO.findByEmail(userDTO.getEmail()).isPresent()) {
            return new ResponseWrapper<>(400, "This email already exists.", null);
        }

        AuthGateway authGateway = factoriaGateway.createGateway(userDTO.getAuthProvider().toString());

        boolean emailValid = authGateway.validateEmail(userDTO.getEmail()).orElse(false);
        if (!emailValid) {
            return new ResponseWrapper<>(400, "Email is not registered with the specified provider.", null);
        }

        boolean passwordValid = authGateway.validatePassword(userDTO.getEmail(), userDTO.getPassword()).orElse(false);
        if (!passwordValid) {
            return new ResponseWrapper<>(401, "Invalid credentials.", null);
        }

        User user = new User(userDTO);
        userDAO.save(user);

        return new ResponseWrapper<>(201, "User registered successfully.", "User ID: " + user.getId());
    }

    // Iniciar sesión de usuario
    public ResponseWrapper<String> loginUser(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        String authProvider = loginDTO.getAuthProvider().toString();

        AuthGateway authGateway = factoriaGateway.createGateway(authProvider);

        boolean credentialsValid = authGateway.validatePassword(email, password).orElse(false);
        if (!credentialsValid) {
            return new ResponseWrapper<>(401, "Invalid credentials.", null);
        }

        Optional<User> optionalUser = userDAO.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return new ResponseWrapper<>(400, "User must be registered first.", null);
        }

        User user = optionalUser.get();

        String token = generateToken();
        UserToken userToken = new UserToken(user, token);
        tokenDAO.save(userToken);

        return new ResponseWrapper<>(200, "Login successful.", token);
    }

    // Cerrar sesión de usuario
    public ResponseWrapper<String> logoutUser(TokenDTO tokenDTO) {
        String token = tokenDTO.getToken();
        Optional<UserToken> optionalToken = tokenDAO.findByToken(token);

        if (optionalToken.isEmpty()) {
            return new ResponseWrapper<>(400, "Invalid token.", null);
        }

        UserToken userToken = optionalToken.get();
        userToken.setRevoked(true);
        tokenDAO.save(userToken);

        return new ResponseWrapper<>(200, "User logged out successfully.", null);
    }

    // Validar el token y obtener el usuario asociado
    public User getUserFromToken(TokenDTO tokenDTO) {
        String token = tokenDTO.getToken();
    
        Optional<User> user = tokenDAO.findUserByToken(token);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid or revoked token.");
        }
    
        return user.get();
    }
    

    // Método para generar un token único basado en el timestamp actual
    private String generateToken() {
        return String.valueOf(System.currentTimeMillis());
    }
}
