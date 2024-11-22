package com.strava.facade;

import com.strava.dto.SessionFilterDTO;
import com.strava.dto.TokenDTO;
import com.strava.dto.TrainingSessionDTO;
import com.strava.service.TrainingSessionService;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sessions")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    public TrainingSessionController(TrainingSessionService trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }

    @Operation(summary = "Create a new training session", description = "Creates a new training session for a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Session created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid data provided"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token")
    })
    @PostMapping
    public ResponseEntity<?> createSession(@RequestParam String token, @RequestBody TrainingSessionDTO session) {
        // Crear un TokenDTO para validación
        TokenDTO tokenDTO = new TokenDTO(token);

        try {
            // Llamar al servicio para crear la sesión, pasando el DTO de la sesión y el token
            String message = trainingSessionService.createSession(tokenDTO, session);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            // Si el error es "Invalid token", retornamos 401 Unauthorized
            if (e.getMessage().equals("Invalid token.")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            // Si el error es otro (ej. datos incorrectos)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Get user training sessions", description = "Fetches training sessions for a user with optional date range and limit.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sessions retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid date format"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token")
    })
    @GetMapping
    public ResponseEntity<?> getUserSessions(@RequestParam String token,
                                            @RequestParam(required = false) String startDate,
                                            @RequestParam(required = false) String endDate,
                                            @RequestParam(required = false) Integer limit) {
        // Crear un FilterDTO con las fechas y el límite
        SessionFilterDTO filterDTO = new SessionFilterDTO();
        try {
            filterDTO.setStartDate(startDate != null ? LocalDate.parse(startDate) : null);
            filterDTO.setEndDate(endDate != null ? LocalDate.parse(endDate) : null);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid date format.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        filterDTO.setLimit(limit != null ? limit : 5);  // Valor por defecto de 5 sesiones si no se pasa un límite

        // Crear un TokenDTO para validación
        TokenDTO tokenDTO = new TokenDTO(token);

        try {
            // Obtener sesiones del servicio
            List<TrainingSessionDTO> sessions = trainingSessionService.getUserSessions(tokenDTO, filterDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("sessions", sessions);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            // Si el error es "Invalid token", retornamos 401 Unauthorized
            if (e.getMessage().equals("Invalid token.")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            // Si el error es otro (ej. datos incorrectos)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Manejo global de IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());

        // Si el error es "Invalid token", retornamos 401 Unauthorized
        if (e.getMessage().equals("Invalid token.")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

