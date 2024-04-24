package com.game.services.models;

import lombok.Getter;

@Getter
public class StatusDTO {
    private int statusCode;
    private String responseMessage;

    public StatusDTO(int statusCode, String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }
}
