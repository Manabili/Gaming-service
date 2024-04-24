package com.game.services.services;

import com.game.services.dao.GameDao;
import com.game.services.models.GameDTO;
import com.game.services.models.ResponseDTO;
import com.game.services.models.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jvnet.hk2.annotations.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class GameService {

    @Inject
    GameDao gameDao;

    public String userLogin(User user) throws SQLException {
        if(!isValidUser(user)) {
            return "Please enter valid Details. Fields can't be blank.";
        }
        int userStatus = gameDao.registerUser(user);
        if(userStatus == 1) {
            return "User has been successfully logged in .";
        }
        else if(userStatus == 2) {
            return "Please enter valid credentials !";
        }
        else if(userStatus == 3){
            return "New user has been successfully registered .";
        }
        return "User is not logged in, Please try again !";
    }

    public String storeGameScores(GameDTO gameDTO) throws SQLException {

        int rowsAffected = gameDao.registerGameScore(gameDTO);
        if(rowsAffected > 0) {
            return "Game Scores has been successfully added .";
        }
        else if(rowsAffected == 0) {
            return "Higher previous game scores !";
        }
        else {
            return "Game Scores is not updated .";
        }

    }

    public List<ResponseDTO> retrieveTopUsers() throws Exception {
        return gameDao.getTopUsers();
    }

    private boolean isValidUser(User user) {
        return !StringUtils.isBlank(user.getUserName()) && !StringUtils.isBlank(user.getUserEmail()) && !StringUtils.isBlank(user.getPassword());
    }
}

