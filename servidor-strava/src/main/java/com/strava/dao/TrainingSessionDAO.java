package com.strava.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.strava.entity.TrainingSession;
import java.util.UUID;

@Repository  // Marca esta clase como un componente DAO de acceso a datos
public interface TrainingSessionDAO extends JpaRepository<TrainingSession, UUID> {

    // Puedes agregar más métodos personalizados si es necesario
    // Por ejemplo, un método para encontrar todas las sesiones de entrenamiento por fecha:
    // List<TrainingSession> findByStartDate(LocalDate startDate);
}
