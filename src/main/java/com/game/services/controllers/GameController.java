package com.game.services.controllers;

import com.game.services.models.CustomResponseDTO;
import com.game.services.models.GameDTO;
import com.game.services.models.ResponseDTO;
import com.game.services.services.GameService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.game.services.models.User;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
public class GameController {
    @Inject
    GameService gameService;

    @PostMapping("/register/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            String status = gameService.userLogin(user);
            return ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (Exception e) {
            log.error("Error in user login : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in user login !");
        }
    }

    @PostMapping("/register/game-score")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> registerGameScore(@RequestBody GameDTO gameDTO) {
        try {
            String status = gameService.storeGameScores(gameDTO);
            return ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (Exception e) {
            log.error("Error in storing Game Scores : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in storing Game Scores !");
        }
    }

    @GetMapping("/getTopUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<CustomResponseDTO> fetchTopUsers() {
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();
        try {
            List<ResponseDTO> topUsers = gameService.retrieveTopUsers();
            customResponseDTO.setResponseDTO(topUsers);
            return ResponseEntity.ok(customResponseDTO);
        } catch (Exception e) {
            log.error("Error in fetching Top Users !");
            customResponseDTO.setErrorMessage("Error in fetching Top Users !");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(customResponseDTO);
        }
    }
}
