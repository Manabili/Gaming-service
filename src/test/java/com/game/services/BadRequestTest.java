package com.game.services;

import com.game.services.controllers.GameController;
import com.game.services.models.CustomResponseDTO;
import com.game.services.models.GameDTO;
import com.game.services.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BadRequestTest {

    @InjectMocks
    GameController gameController;

    @Test(expected = Exception.class)
    public void testUserRegisterError() throws Exception {
        Mockito.when(gameController.registerUser(new User())).thenThrow(new Exception());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), gameController.registerUser(new User()).getStatusCode().value());
    }

    @Test(expected = Exception.class)
    public void testFetchUserError() throws Exception {
        Mockito.when(gameController.findUser(new User())).thenThrow(new Exception());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), gameController.findUser(new User()).getStatusCode().value());
    }

    @Test(expected = Exception.class)
    public void testStoreGameScoreError() throws Exception {
        Mockito.when(gameController.registerGameScore(new GameDTO())).thenThrow(new Exception());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), gameController.registerGameScore(new GameDTO()).getStatusCode().value());
    }

    @Test(expected = Exception.class)
    public void testFetchUserScoreError() throws Exception {
            Mockito.when(gameController.fetchUserScore(1)).thenThrow(new Exception());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), gameController.fetchUserScore(1).getStatusCode().value());
    }

    @Test(expected = Exception.class)
    public void testFetchTopUserError() throws Exception {
        Mockito.when(gameController.fetchTopUsers()).thenThrow(new Exception());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), gameController.fetchTopUsers().getStatusCode().value());
    }

}
