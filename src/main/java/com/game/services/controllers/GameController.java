package com.game.services.controllers;

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
            if(!isValidUser(user)) {
                return ResponseEntity.ok("Please enter valid Details. Fields can't be blank.");
            }
            gameService.userLogin(user);
            return ResponseEntity.status(HttpStatus.OK).body("User has been logged in successfully !");
        } catch (Exception e) {
            log.error("Error in user login : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in user login : " + e.getMessage());
        }
    }

    @PostMapping("/register/game-score")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> registerGameScore(@RequestBody GameDTO gameDTO) {
        try {
            gameService.storeGameScores(gameDTO);
            return ResponseEntity.status(HttpStatus.OK).body("User's score has been stored successfully !");
        } catch (Exception e) {
            log.error("Error in storing scores!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in storing users score : " + e.getMessage());
        }
    }

    @GetMapping("/getTopUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<ResponseDTO>> fetchTopUsers() {
        List<ResponseDTO> topUsers = null;
        try {
            topUsers = gameService.retrieveTopUsers();
        } catch (Exception e) {
            log.error("Error in user login!");
        }
        return ResponseEntity.ok(topUsers);
    }

    private boolean isValidUser(User user) {
        return !StringUtils.isBlank(user.getUserName()) && !StringUtils.isBlank(user.getUserEmail()) && !StringUtils.isBlank(user.getPassword());
    }
}
