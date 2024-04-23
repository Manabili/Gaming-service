package com.game.services.dao;

import com.game.services.config.Constants;
import com.game.services.config.DatabaseClient;
import com.game.services.models.GameDTO;
import com.game.services.models.ResponseDTO;
import com.game.services.models.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class GameDao {

    DatabaseClient databaseClient;
    Connection gamesDb = null;

    public GameDao() {
        try {
            databaseClient = DatabaseClient.getInstance();
            gamesDb = databaseClient.getConnection(Constants.GAMES_DB);
        } catch (Exception e) {
            log.error("Error connecting to database : {}", e.getMessage());
        }
    }

    public void registerUser(User user) throws SQLException {
        if(isUserExists(user)) {
            if (checkUserCredentials(user)) {
                log.info("User has been successfully logged in");
            } else {
                log.error("Please enter valid credentials !");
            }
        }
        else {

            String query = "INSERT INTO USERS(userName, userEmail, password) values(?, ?, ?)";

            // Create a prepared statement for the query
            PreparedStatement preparedStatement = gamesDb.prepareStatement(query);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getUserEmail());
            preparedStatement.setString(3, user.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();
            log.info("New user has been successfully registered");
        }
    }

    public boolean isUserExists(User user) throws SQLException {

        String query = "SELECT count(1) from USERS where userEmail = ?";

        PreparedStatement preparedStatement = gamesDb.prepareStatement(query);
        preparedStatement.setString(1, user.getUserEmail());

        ResultSet resultSet = preparedStatement.executeQuery();
        int userCount = 0;
        if (resultSet.next()) {
            userCount = resultSet.getInt(1);
        }
        return userCount > 0;
    }

    public boolean checkUserCredentials(User user) throws SQLException {

        String query = "SELECT count(1) from USERS where userEmail = ? and password = ?";

        PreparedStatement preparedStatement = gamesDb.prepareStatement(query);
        preparedStatement.setString(1, user.getUserEmail());
        preparedStatement.setString(2, user.getPassword());

        ResultSet resultSet = preparedStatement.executeQuery();

        int userCount = 0;
        if (resultSet.next()) {
            userCount = resultSet.getInt(1);
        }
        return userCount > 0;
    }

    public void registerGameScore(GameDTO gameDTO) throws SQLException {

        String query = "UPDATE GAME_SCORES SET game_score = ? where userId = ? ";
        // Create a prepared statement for the query
        PreparedStatement preparedStatement = gamesDb.prepareStatement(query);
        preparedStatement.setInt(1, gameDTO.getGameScore());
        preparedStatement.setInt(2, gameDTO.getUserId());

        int rowsAffected = preparedStatement.executeUpdate();
        if(rowsAffected > 0) {
            log.info("Game Score has been successfully added !");
        }
    }
    public List<ResponseDTO> getTopUsers() throws Exception {
        String query = "SELECT u.userId, u.userName, u.userEmail, g.game_score, g.game_timestamp FROM users u " +
                "JOIN game_scores g ON u.userId = g.userId " +
                "ORDER BY g.game_score DESC, g.game_timestamp ASC limit 5;";

        PreparedStatement preparedStatement = gamesDb.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        return buildResponse(resultSet);
    }

    public List<ResponseDTO> buildResponse(ResultSet resultSet) throws Exception {
        List<ResponseDTO> topUsers = new ArrayList<>();
        while (resultSet.next()) {
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setUserName(resultSet.getString("userName"));
            responseDTO.setUserEmail(resultSet.getString("userEmail"));
            responseDTO.setGameScore(resultSet.getInt("game_score"));
            responseDTO.setTimeStamp(resultSet.getTimestamp("game_timestamp"));
            topUsers.add(responseDTO);
        }
        return topUsers;
    }
}
