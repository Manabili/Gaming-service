package com.game.services.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.services.dao.GameDao;
import com.game.services.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class GameService {

    @Inject
    GameDao gameDao;

    public StatusDTO userLogin(User user) throws SQLException {
        if(!isValidUser(user)) {
            return new StatusDTO(-1, "Please enter valid Details. Fields can't be blank.");
        }
        int userStatus = gameDao.registerUser(user);
        if(userStatus == 1) {
            return new StatusDTO(userStatus,"User has been successfully logged in.");
        }
        else if(userStatus == 2) {
            return new StatusDTO(userStatus,"Please enter valid credentials !");
        }
        else if(userStatus == 3){
            return new StatusDTO(userStatus,"New user has been successfully registered.");
        }
        return new StatusDTO(userStatus, "User is not logged in, Please try again !");
    }

    public StatusDTO storeGameScores(GameDTO gameDTO) throws SQLException {

        int rowsAffected = gameDao.registerGameScore(gameDTO);
        if(rowsAffected > 0) {
            return new StatusDTO(1,"Game Scores has been successfully added.");
        }
        else if(rowsAffected == 0) {
            return new StatusDTO(2,"Higher previous game scores !");
        }
        else {
            return new StatusDTO(0, "Game Scores is not updated.");
        }
    }

    public CustomResponseDTO retrieveTopUsers() throws Exception {
        CustomResponseDTO topUsers = new CustomResponseDTO();
        List<ResponseDTO> responseDTOS = gameDao.getTopUsers();
        topUsers.setResponseDTO(responseDTOS);
        log.info("Top Users : {}", new ObjectMapper().writeValueAsString(topUsers));
        return topUsers;
    }

    private boolean isValidUser(User user) {
        return !StringUtils.isBlank(user.getUserName()) && !StringUtils.isBlank(user.getUserEmail()) && !StringUtils.isBlank(user.getPassword());
    }
}

