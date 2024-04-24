package com.game.services.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.services.models.CustomResponseDTO;
import com.game.services.models.GameDTO;
import com.game.services.models.StatusDTO;
import com.game.services.models.User;
import com.game.services.services.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Controller
@Slf4j
public class GameController {
    @Inject
    GameService gameService;

    @Inject
    ObjectMapper objectMapper;

    @PostMapping("/register/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            log.info("User Details : {}", objectMapper.writeValueAsString(user));
            StatusDTO statusDTO = gameService.userLogin(user);
            if(statusDTO.getStatusCode() == -1 || statusDTO.getStatusCode() == 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusDTO.getResponseMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).body(statusDTO.getResponseMessage());
        } catch (Exception e) {
            log.error("Error in user login : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in user login !");
        }
    }


    @PostMapping("/fetch/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> findUser(@RequestBody(required = true) User user) {
        try {
            log.info("User Details : {}", objectMapper.writeValueAsString(user));
            int userId = gameService.getUserId(user);
            if(userId > 0) {
                return ResponseEntity.status(HttpStatus.OK).body(Integer.toString(userId));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found !");
        } catch (Exception e) {
            log.error("Error in fetching user : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in user login !");
        }
    }

    @PostMapping("/register/game-score")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> registerGameScore(@RequestBody GameDTO gameDTO) {
        try {
            log.info("Game Details : {}", objectMapper.writeValueAsString(gameDTO));
            StatusDTO statusDTO = gameService.storeGameScores(gameDTO);
            if(statusDTO.getStatusCode() > 0) {
                return ResponseEntity.status(HttpStatus.OK).body(statusDTO.getResponseMessage());
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(statusDTO.getResponseMessage());
            }
        } catch (Exception e) {
            log.error("Error in storing Game Scores : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in storing Game Scores !");
        }
    }
    @GetMapping("/getTopUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<CustomResponseDTO> fetchTopUsers() {
        log.info("Request received for fetching Top Users !");
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();
        try {
            customResponseDTO = gameService.retrieveTopUsers();
            return ResponseEntity.ok(customResponseDTO);
        } catch (Exception e) {
            log.error("Error in fetching Top Users !");
            customResponseDTO.setErrorMessage("Error in fetching Top Users !");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(customResponseDTO);
        }
    }

    @GetMapping("/user/score")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<CustomResponseDTO> fetchUserScore(@RequestParam(required = true) int userId) {
        log.info("Request received for fetching Top Users !");
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();
        try {
            customResponseDTO = gameService.fetchUserHighestScore(userId);
            return ResponseEntity.ok(customResponseDTO);
        } catch (Exception e) {
            log.error("Error in fetching User's Game Details !");
            customResponseDTO.setErrorMessage("Error in fetching User's Game Details !");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(customResponseDTO);
        }
    }
}
