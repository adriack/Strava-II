package com.strava.facade;

import com.strava.dto.ChallengeDTO;
import com.strava.dto.ChallengeFilterDTO;
import com.strava.dto.TokenDTO;
import com.strava.entity.SportType;
import com.strava.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @Operation(summary = "Create a new challenge", description = "Creates a new challenge for a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Challenge created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid data provided"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token")
    })
    @PostMapping
    public ResponseEntity<?> createChallenge(@RequestParam String token, @RequestBody ChallengeDTO challengeDTO) {
        TokenDTO tokenDTO = new TokenDTO(token);

        try {
            String message = challengeService.createChallenge(tokenDTO, challengeDTO);
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

    @Operation(summary = "Get active challenges", description = "Fetches active challenges based on filters like date and sport.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Challenges retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid date format"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token")
    })
    @GetMapping
    public ResponseEntity<?> getActiveChallenges(@RequestParam(required = false) String date,
                                                 @RequestParam(required = false) SportType sport,
                                                 @RequestParam(required = false, defaultValue = "5") Integer limit) {
        ChallengeFilterDTO filterDTO = new ChallengeFilterDTO();
        try {
            filterDTO.setDate(date != null ? LocalDate.parse(date) : LocalDate.now());
            filterDTO.setSport(sport);
            filterDTO.setLimit(limit);

            List<ChallengeDTO> challenges = challengeService.getActiveChallenges(filterDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("challenges", challenges);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid date format.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Accept a challenge", description = "Accepts a challenge for a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Challenge accepted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token")
    })
    @PostMapping("/{challengeId}/accept")
    public ResponseEntity<?> acceptChallenge(@PathVariable String challengeId, @RequestParam String token) {
        TokenDTO tokenDTO = new TokenDTO(token);

        try {
            String message = challengeService.acceptChallenge(tokenDTO, challengeId);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            // Si el error es "Invalid token", retornamos 401 Unauthorized
            if (e.getMessage().equals("Invalid token.")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Get accepted challenges", description = "Fetches challenges accepted by a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Accepted challenges retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token")
    })
    @GetMapping("/accepted")
    public ResponseEntity<?> getAcceptedChallenges(@RequestParam String token) {
        TokenDTO tokenDTO = new TokenDTO(token);

        try {
            List<ChallengeDTO> challenges = challengeService.getAcceptedChallenges(tokenDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("challenges", challenges);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            // Si el error es "Invalid token", retornamos 401 Unauthorized
            if (e.getMessage().equals("Invalid token.")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

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

