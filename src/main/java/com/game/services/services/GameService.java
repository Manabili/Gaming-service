package com.game.services.services;

import com.game.services.dao.GameDao;
import com.game.services.models.GameDTO;
import com.game.services.models.ResponseDTO;
import com.game.services.models.User;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class GameService {

    @Inject
    GameDao gameDao;

    public void userLogin(User user) throws SQLException {
        gameDao.registerUser(user);
    }

    public void storeGameScores(GameDTO gameDTO) throws SQLException {
        gameDao.registerGameScore(gameDTO);
    }

    public List<ResponseDTO> retrieveTopUsers() throws Exception {
        return gameDao.getTopUsers();
    }
}

