package com.strava.service;

import com.strava.dto.ChallengeDTO;
import com.strava.dto.ChallengeFilterDTO;
import com.strava.dto.TokenDTO;
import com.strava.entity.Challenge;
import com.strava.entity.User;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {

    private final UserService userService;
    private final Map<UUID, Challenge> challengeDatabase = new HashMap<>();

    public ChallengeService(UserService userService) {
        this.userService = userService;
    }

    // Crear un reto y almacenarlo en el HashMap
    public String createChallenge(TokenDTO tokenDTO, ChallengeDTO challengeDTO) {
        User user = userService.getUserFromToken(tokenDTO);
        if (user == null) {
            throw new IllegalArgumentException("Invalid token.");
        }

        // Crear un nuevo ID para el reto
        UUID challengeId = UUID.randomUUID();
        challengeDTO.setId(challengeId);

        // Crear un nuevo objeto Challenge a partir del DTO
        Challenge challenge = new Challenge(challengeDTO);

        // Guardar el reto en la base de datos (HashMap)
        challengeDatabase.put(challengeId, challenge);

        return "Challenge created successfully with ID: " + challengeId;
    }

    // Obtener retos activos según los filtros especificados
    public List<ChallengeDTO> getActiveChallenges(ChallengeFilterDTO challengeFilterDTO) {
        LocalDate date = challengeFilterDTO.getDate();

        // Filtrar los retos activos basados en la fecha y en el tipo de deporte
        return challengeDatabase.values().stream()
                .filter(challenge -> challenge.getEndDate().isAfter(date) || challenge.getEndDate().isEqual(date))
                .filter(challenge -> challenge.getStartDate().isBefore(date) || challenge.getEndDate().isEqual(date))
                .filter(challenge -> challengeFilterDTO.getSport() == null || challenge.getSport().equals(challengeFilterDTO.getSport()))
                .limit(challengeFilterDTO.getLimit())
                .map(challenge -> new ChallengeDTO(challenge))
                .collect(Collectors.toList());
    }

    // Aceptar un reto y asociarlo al usuario
    public String acceptChallenge(TokenDTO tokenDTO, String challengeId) {
        User user = userService.getUserFromToken(tokenDTO);
        if (user == null) {
            throw new IllegalArgumentException("Invalid token.");
        }

        UUID challengeUUID = UUID.fromString(challengeId);
        Challenge challenge = challengeDatabase.get(challengeUUID);

        if (challenge == null) {
            throw new IllegalArgumentException("Challenge not found.");
        }

        // Asociación del reto y el usuario
        user.addChallengeId(challengeUUID);
        challenge.addUserId(user.getId());

        return "Challenge accepted.";
    }

    // Obtener todos los retos aceptados por el usuario
    public List<ChallengeDTO> getAcceptedChallenges(TokenDTO tokenDTO) {
        User user = userService.getUserFromToken(tokenDTO);
        if (user == null) {
            throw new IllegalArgumentException("Invalid token.");
        }

        // Obtener los retos aceptados por el usuario a partir de los IDs de retos en el usuario
        return user.getChallengeIds().stream()
                .map(challengeDatabase::get)
                .filter(Objects::nonNull) // Evitar valores nulos en caso de IDs no válidos
                .map(challenge -> new ChallengeDTO(challenge))
                .collect(Collectors.toList());
    }
}