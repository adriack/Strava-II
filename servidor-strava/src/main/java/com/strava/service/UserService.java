package com.strava.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strava.dao.TokenDAO;
import com.strava.dao.UserDAO;
import com.strava.dto.LoginDTO;
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
    public String registerUser(UserDTO userDTO) {
        // Verificar si ya existe un usuario con el mismo correo
        if (userDAO.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email already exists.");
        }

        // Usar la factoría para obtener el gateway adecuado
        AuthGateway authGateway = factoriaGateway.createGateway(userDTO.getAuthProvider().toString());

        // Validar el email con el proveedor correspondiente
        boolean emailValid = authGateway.validateEmail(userDTO.getEmail()).orElse(false);
        if (!emailValid) {
            throw new IllegalArgumentException("Email is not registered with the specified provider.");
        }

        // Crear un nuevo usuario
        User user = new User(userDTO);

        // Guardar el usuario en la base de datos
        userDAO.save(user);

        return "User registered successfully with ID: " + user.getId();
    }

    // Iniciar sesión de usuario
    public String loginUser(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        String authProvider = loginDTO.getAuthProvider().toString();

        // Usar la factoría para obtener el gateway adecuado
        AuthGateway authGateway = factoriaGateway.createGateway(authProvider);

        // Validar las credenciales con el proveedor correspondiente
        boolean credentialsValid = authGateway.validatePassword(email, password).orElse(false);
        if (!credentialsValid) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        // Buscar al usuario por su email
        Optional<User> optionalUser = userDAO.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User must be registered first.");
        }

        User user = optionalUser.get();

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

    // Método para generar un token único basado en el timestamp actual
    private String generateToken() {
        return String.valueOf(System.currentTimeMillis());
    }

}
