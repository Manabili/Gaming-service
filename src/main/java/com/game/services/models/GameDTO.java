package com.game.services.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class GameDTO {
    private int userId;
    private int gameScore;
    private Timestamp timeStamp;
}
