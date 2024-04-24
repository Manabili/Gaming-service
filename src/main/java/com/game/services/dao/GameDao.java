package com.game.services.dao;

import com.game.services.config.Constants;
import com.game.services.config.DatabaseClient;
import com.game.services.models.GameDTO;
import com.game.services.models.ResponseDTO;
import com.game.services.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public int registerUser(User user) throws SQLException {
        if(isUserExists(user) > 0) {
            if (checkUserCredentials(user)) {
                log.info("User has been successfully logged in");
                return 1;
            } else {
                log.error("Please enter valid credentials !");
                return 2;
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
            if(rowsAffected > 0) {
                log.info("New user has been successfully registered .");
                return 3;
            }
        }
        return 0;
    }

    public int registerGameScore(GameDTO gameDTO) throws SQLException {
        int game_score = getPreviousScore(gameDTO.getUserId());
        if(gameDTO.getGameScore() > game_score) {
            String query = "REPLACE INTO GAME_SCORES(userId, game_score) VALUES(?, ?) ";
            // Create a prepared statement for the query
            PreparedStatement preparedStatement = gamesDb.prepareStatement(query);
            preparedStatement.setInt(1, gameDTO.getUserId());
            preparedStatement.setInt(2, gameDTO.getGameScore());

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0) {
                log.info("Game Score has been successfully added.");
                return rowsAffected;
            }
            log.error("Game Score is not updated.");
            return -1;
        }
        else {
            log.info("Higher previous game score than the current one !");
            return 0;
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

    public List<ResponseDTO> fetchUserScore(int userId) throws Exception {
        String query = "SELECT u.userId, u.userName, u.userEmail, g.game_score, g.game_timestamp FROM users u " +
                "JOIN game_scores g ON u.userId = g.userId where g.userId = ?;";

        PreparedStatement preparedStatement = gamesDb.prepareStatement(query);
        preparedStatement.setInt(1, userId);

        ResultSet resultSet = preparedStatement.executeQuery();
        return buildResponse(resultSet);
    }

    public int isUserExists(User user) throws SQLException {
        String query = "SELECT userId from USERS where userEmail = ?";
        PreparedStatement preparedStatement = gamesDb.prepareStatement(query);
        preparedStatement.setString(1, user.getUserEmail());
        ResultSet resultSet = preparedStatement.executeQuery();
        int userCount = 0;
        if (resultSet.next()) {
            userCount = resultSet.getInt(1);
        }
        return userCount;
    }

    private boolean checkUserCredentials(User user) throws SQLException {
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

    private int getPreviousScore(int userId) throws SQLException {
        String query = "SELECT game_score FROM game_scores WHERE userId = ? ";
        PreparedStatement preparedStatement = gamesDb.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("game_score");
        } else {
            // If no previous score found, return a default value (e.g., 0)
            return 0;
        }
    }
    private List<ResponseDTO> buildResponse(ResultSet resultSet) throws Exception {
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
