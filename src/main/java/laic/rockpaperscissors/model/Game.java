package laic.rockpaperscissors.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import laic.rockpaperscissors.model.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Game {
    private UUID id;
    private Player player1;
    private Player player2;
    private GameStatus status;
    private String result;
}
