package com.strava.dao;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import com.strava.entity.User;

@Repository  // Marca esta clase como un componente DAO de acceso a datos.
public interface UserDAO extends JpaRepository<User, UUID> {

    // Método para buscar un usuario por su correo electrónico
    Optional<User> findByEmail(String email);

}

