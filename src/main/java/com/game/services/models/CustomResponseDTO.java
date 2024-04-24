package com.game.services.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponseDTO {
    private List<ResponseDTO> responseDTO;
    private String errorMessage;
}
