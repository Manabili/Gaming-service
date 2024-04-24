package com.game.services;

import com.game.services.dao.GameDao;
import com.game.services.models.GameDTO;
import com.game.services.models.ResponseDTO;
import com.game.services.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class GameDaoTest {

    public static final Logger log = LoggerFactory.getLogger(GameDaoTest.class);

    @Mock
    GameDao gameDao;

    @Test
    public void testUserLogin() {
        User user = new User();
        user.setUserName("Herny");
        user.setUserEmail("herny@gmail.com");
        user.setPassword("aZxdsfsfklf");

        try {
            Mockito.when(gameDao.registerUser(user)).thenReturn(3);
            assertEquals(gameDao.registerUser(user), 3);
        } catch (Exception e) {
            log.error("Error in registering test User !");
        }
    }

    @Test
    public void testFetchUser() {
        User user = new User();
        user.setUserEmail("mark@gmail.com");

        try {
            Mockito.when(gameDao.isUserExists(user)).thenReturn(2);
            assertEquals(gameDao.isUserExists(user), 2);
        } catch (Exception e) {
            log.error("Error in fetching test User !");
        }
    }
    @Test
    public void testStoreGameScore() {
        GameDTO gameDTO = new GameDTO();
        try {
            Mockito.when(gameDao.registerGameScore(gameDTO)).thenReturn(1);
            assertEquals(gameDao.registerGameScore(gameDTO), 1);
        } catch (Exception e) {
            log.error("Error in storing game scores !");
        }
    }

    @Test
    public void testTopUser() {
        GameDTO gameDTO = new GameDTO();
        List<ResponseDTO> topUsers = new ArrayList<>();
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setGameScore(600);
        responseDTO.setUserName("Harry");
        responseDTO.setUserEmail("Harry@gmail.com");
        topUsers.add(responseDTO);

        try {
            Mockito.when(gameDao.getTopUsers()).thenReturn(topUsers);
            assertNotNull(gameDao.getTopUsers());
        } catch (Exception e) {
            log.error("Error in getting Top Users !");
        }
    }

    @Test
    public void testUserScore() {
        List<ResponseDTO> userScore = new ArrayList<>();
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setGameScore(600);
        responseDTO.setUserName("Harry");
        responseDTO.setUserEmail("Harry@gmail.com");
        userScore.add(responseDTO);

        try {
            Mockito.when(gameDao.fetchUserScore(2)).thenReturn(userScore);
            assertNotNull(gameDao.fetchUserScore(2));
        } catch (Exception e) {
            log.error("Error in getting User Scores !");
        }
    }
}
