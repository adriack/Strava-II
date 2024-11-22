package com.strava.service;

import com.strava.dto.SessionFilterDTO;
import com.strava.dto.TrainingSessionDTO;
import com.strava.dto.TokenDTO;
import com.strava.entity.TrainingSession;
import com.strava.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class TrainingSessionService {

    // Almacén para sesiones de entrenamiento
    private Map<UUID, TrainingSession> sessionDatabase = new HashMap<>();

    // Referencia al UserService para acceder a los usuarios y al tokenDatabase
    private final UserService userService;

    public TrainingSessionService(UserService userService) {
        this.userService = userService;
    }

    // Crear una nueva sesión de entrenamiento
    public String createSession(TokenDTO tokenDTO, TrainingSessionDTO sessionDTO) {
        // Validar token y obtener el usuario
        User user = userService.getUserFromToken(tokenDTO);

        if (user == null) {
            throw new IllegalArgumentException("Invalid token.");
        }

        // Crear un nuevo ID para la sesión
        UUID sessionId = UUID.randomUUID();
        sessionDTO.setId(sessionId);

        // Crear el objeto TrainingSession a partir del DTO
        TrainingSession session = new TrainingSession(sessionDTO);

        // Guardar la sesión en el HashMap
        sessionDatabase.put(sessionId, session);

        // Asociar el ID de la sesión al usuario
        user.addSessionId(sessionId);

        return "Training session created successfully with ID: " + sessionId;
    }

    public List<TrainingSessionDTO> getUserSessions(TokenDTO tokenDTO, SessionFilterDTO filterDTO) {
        // Validar token y obtener el usuario
        User user = userService.getUserFromToken(tokenDTO);

        if (user == null) {
            throw new IllegalArgumentException("Invalid token.");
        }

        // Obtener los IDs de las sesiones asociadas al usuario
        List<UUID> userSessionIds = user.getSessionIds();

        // Filtrar las sesiones de entrenamiento que están asociadas a los SessionIds del usuario
        List<TrainingSession> sessions = sessionDatabase.values().stream()
            .filter(session -> userSessionIds.contains(session.getId()))  // Filtramos por las sesiones del usuario
            .collect(Collectors.toList());

        // Filtrar por fechas (si se proporcionan)
        if (filterDTO.getStartDate() != null) {
            sessions = sessions.stream()
                .filter(session -> session.getStartDate().isAfter(filterDTO.getStartDate()) || session.getStartDate().isEqual(filterDTO.getStartDate()))
                .collect(Collectors.toList());
        }

        if (filterDTO.getEndDate() != null) {
            sessions = sessions.stream()
                .filter(session -> session.getStartDate().isBefore(filterDTO.getEndDate()) || session.getStartDate().isEqual(filterDTO.getEndDate()))
                .collect(Collectors.toList());
        }

        // Limitar la cantidad de resultados según el parámetro `limit`
        Integer limit = filterDTO.getLimit();
        if (sessions.size() > limit) {
            sessions = sessions.subList(0, limit);  // Tomamos solo los primeros `limit` elementos
        }

        // Convertir las sesiones de entrenamiento a DTOs
        List<TrainingSessionDTO> sessionDTOs = sessions.stream()
            .map(session -> new TrainingSessionDTO(session))
            .collect(Collectors.toList());

        return sessionDTOs;
    }
}
