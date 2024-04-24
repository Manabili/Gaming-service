package com.game.services.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private long userId;
    private String userName;
    private String userEmail;
    private String password;
}
