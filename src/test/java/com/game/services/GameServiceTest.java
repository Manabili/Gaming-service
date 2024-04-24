package com.game.services;

import com.game.services.config.Constants;
import com.game.services.config.DatabaseClient;
import com.game.services.models.GameDTO;
import com.game.services.models.User;
import com.game.services.services.GameService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameServiceTest {

    public static final Logger log = LoggerFactory.getLogger(GameDaoTest.class);
    @Inject
    GameService gameService;
    @Before
    public void setUp() throws Exception {
        DatabaseClient databaseClient = DatabaseClient.getInstance();
        Connection connection = databaseClient.getConnection(Constants.GAMES_DB);
        String deleteUser = "DELETE from USERS where userEmail = 'herny@gmail.com'";
        String deleteGameScore = "DELETE from GAME_SCORES where userId = 1";

        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteUser);
        statement.executeUpdate(deleteGameScore);
    }

    @Test
    public void testUserLogin() {
        User user = new User();
        user.setUserName("Herny");
        user.setUserEmail("herny@gmail.com");
        user.setPassword("aZxdsfsfklf");

        try {
            assertEquals(gameService.userLogin(user).getStatusCode(), 3);
        } catch (Exception e) {
            log.error("Error in registering test User !");
        }

        try {
            assertEquals(gameService.userLogin(user).getStatusCode(), 1);
        } catch (Exception e) {
            log.error("Error logging in test User !");
        }

        try {
            assertEquals(gameService.userLogin(user).getStatusCode(), 1);
        } catch (Exception e) {
            log.error("Error logging in test User !");
        }
    }

    @Test
    public void testFetchUser() {
        User user = new User();
        user.setUserEmail("mark@gmail.com");

        try {
            assertNotEquals(gameService.getUserId(user), 0);
        } catch (Exception e) {
            log.error("Error in registering test User !");
        }

        user.setUserEmail("abcxyz636");
        try {
            assertEquals(gameService.getUserId(user), 0);
        } catch (Exception e) {
            log.error("Error logging in test User !");
        }

    }

    @Test
    public void testStoreGameScore() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setUserId(1);
        gameDTO.setGameScore(4543);
        try {
            assertEquals(gameService.storeGameScores(gameDTO).getStatusCode(), 1);
        } catch (Exception e) {
            log.error("Error in storing game scores !");
        }

        gameDTO.setGameScore(454);
        try {
            assertEquals(gameService.storeGameScores(gameDTO).getStatusCode(), 2);
        } catch (Exception e) {
            log.error("Error in storing game scores !");
        }

        gameDTO.setUserId(0);
        gameDTO.setGameScore(454);
        try {
            assertEquals(gameService.storeGameScores(gameDTO).getStatusCode(), 2);
        } catch (Exception e) {
            log.error("Error in storing game scores !");
        }
    }

    @Test
    public void testTopUser() {
        try {
            assertNotNull(gameService.retrieveTopUsers().getResponseDTO());
        } catch (Exception e) {
            log.error("Error in getting Top Users !");
        }
    }

    @Test
    public void testUserScore() {
        try {
            assertNotNull(gameService.fetchUserHighestScore(2).getResponseDTO());
        } catch (Exception e) {
            log.error("Error in getting User Score !");
        }
    }
}
