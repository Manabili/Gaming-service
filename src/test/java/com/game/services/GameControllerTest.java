package com.game.services;

import com.game.services.config.Constants;
import com.game.services.config.DatabaseClient;
import com.game.services.models.CustomResponseDTO;
import com.game.services.models.GameDTO;
import com.game.services.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerTest {
    public static final Logger log = LoggerFactory.getLogger(GameDaoTest.class);
    @Inject
    TestRestTemplate restTemplate;
    HttpHeaders headers = null;
    @Before
    public void setUp() throws Exception {
        headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        DatabaseClient databaseClient = DatabaseClient.getInstance();
        Connection connection = databaseClient.getConnection(Constants.GAMES_DB);
        String deleteUser = "DELETE from USERS where userEmail = 'herny@gmail.com'";
        String deleteGameScore = "DELETE from GAME_SCORES where userId = 1";

        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteUser);
        statement.executeUpdate(deleteGameScore);
    }

    @Test
    public void registerUserTest() {
        User user = new User();
        user.setUserName("Herny");
        user.setUserEmail("herny@gmail.com");
        user.setPassword("aZxdsfsfklf");

        String url = "/register/user";
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        user.setPassword("");
        requestEntity = new HttpEntity<>(user, headers);
        responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());

    }

    @Test
    public void fetchUserTest() {
        User user = new User();
        user.setUserEmail("mark@gmail.com");

        String url = "/fetch/user";
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        user.setUserEmail("abc@gmail.com");
        requestEntity = new HttpEntity<>(user, headers);
        responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCode().value());

    }

    @Test
    public void registerGameScoreTest() {
        String url = "/register/game-score";
        GameDTO gameDTO = new GameDTO();
        gameDTO.setUserId(1);
        gameDTO.setGameScore(4543);

        HttpEntity<GameDTO> requestEntity = new HttpEntity<>(gameDTO, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        gameDTO.setUserId(-1);
        requestEntity = new HttpEntity<>(gameDTO, headers);
        responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCode().value());
    }

    @Test
    public void fetchTopUsersTest() {
        String url = "/getTopUsers";
        ResponseEntity<CustomResponseDTO> responseEntity = restTemplate.getForEntity(url, CustomResponseDTO.class);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
    }

    @Test
    public void fetchUserScoreTest() {
        String url = "/user/score?userId=2";
        ResponseEntity<CustomResponseDTO> responseEntity = restTemplate.getForEntity(url, CustomResponseDTO.class);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
    }
}
