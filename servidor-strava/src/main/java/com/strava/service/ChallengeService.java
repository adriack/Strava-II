package com.strava.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.strava.dao.ChallengeDAO;
import com.strava.dto.ChallengeDTO;
import com.strava.dto.ChallengeFilterDTO;
import com.strava.dto.TokenDTO;
import com.strava.entity.Challenge;
import com.strava.entity.User;
import com.strava.entity.enumeration.SportType;

@Service
public class ChallengeService {

    private final UserService userService;
    private final ChallengeDAO challengeDAO;

    public ChallengeService(UserService userService, ChallengeDAO challengeDAO) {
        this.userService = userService;
        this.challengeDAO = challengeDAO;
    }

    // Crear un reto y almacenarlo en la base de datos
    public String createChallenge(TokenDTO tokenDTO, ChallengeDTO challengeDTO) {
        User user = userService.getUserFromToken(tokenDTO);

        // Validar que la fecha de fin sea posterior o igual a la fecha de inicio
        LocalDate startDate = challengeDTO.getStartDate();
        LocalDate endDate = challengeDTO.getEndDate();

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        // Crear un nuevo objeto Challenge a partir del DTO
        Challenge challenge = new Challenge(challengeDTO);
        challenge.addUser(user); // Asociar el usuario al reto

        // Guardar el reto en la base de datos
        challengeDAO.save(challenge);

        return "Challenge created successfully with ID: " + challenge.getId();
    }

    public List<ChallengeDTO> getActiveChallenges(ChallengeFilterDTO challengeFilterDTO) {
        LocalDate startDate = challengeFilterDTO.getStartDate();
        LocalDate endDate = challengeFilterDTO.getEndDate();
        SportType sport = challengeFilterDTO.getSport();
        
        // Establecer un límite por defecto de 5 si el valor de limit es null
        if (challengeFilterDTO.getLimit() == null) {
            challengeFilterDTO.setLimit(5);
        }
    
        // Comprobar que endDate es posterior o igual a startDate
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be greater than or equal to start date.");
        }
    
        Pageable pageable = PageRequest.of(0, challengeFilterDTO.getLimit());
    
        // Consultar los retos filtrados desde la base de datos
        Page<Challenge> challengesPage = challengeDAO.findFilteredChallenges(
                null, startDate, endDate, sport, pageable
        );
    
        // Convertir las entidades de Challenge a DTOs
        return challengesPage.getContent().stream()
                .map(challenge -> new ChallengeDTO(challenge))
                .collect(Collectors.toList());
    }    

    // Aceptar un reto y asociarlo al usuario
    public String acceptChallenge(TokenDTO tokenDTO, String challengeId) {
        User user = userService.getUserFromToken(tokenDTO);

        UUID challengeUUID = UUID.fromString(challengeId);
        Challenge challenge = challengeDAO.findById(challengeUUID)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found."));

        // Asociación del reto y el usuario
        challenge.addUser(user);

        // Guardar los cambios en la base de datos
        challengeDAO.save(challenge);

        return "Challenge accepted.";
    }

    // Obtener todos los retos aceptados por el usuario
    public List<ChallengeDTO> getAcceptedChallenges(TokenDTO tokenDTO) {
        User user = userService.getUserFromToken(tokenDTO);
        if (user == null) {
            throw new IllegalArgumentException("Invalid token.");
        }

        // Consultar los retos aceptados desde la base de datos
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);  // Recuperamos todos los retos sin paginación
        Page<Challenge> challengesPage = challengeDAO.findFilteredChallenges(
                user.getId(), null, null, null, pageable
        );

        // Convertir las entidades de Challenge a DTOs
        return challengesPage.getContent().stream()
                .map(challenge -> new ChallengeDTO(challenge))
                .collect(Collectors.toList());
    }
    
}
