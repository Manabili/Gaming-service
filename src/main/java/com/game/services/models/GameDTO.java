package com.game.services.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class GameDTO {
    private int userId;
    private int gameScore;
    private Timestamp timeStamp;
}
