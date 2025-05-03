package laic.rockpaperscissors.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import laic.rockpaperscissors.model.enums.GameStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameDTO {
    private UUID id;
    private String player1;
    private String player2;
    private String player1Move;
    private String player2Move;
    private GameStatus status;
    private String result;
}