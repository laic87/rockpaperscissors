package laic.rockpaperscissors.service;

import laic.rockpaperscissors.dto.GameDTO;
import laic.rockpaperscissors.model.Game;
import laic.rockpaperscissors.model.enums.Move;

import java.util.UUID;

public interface GameService {
    UUID createGame(String name);
    Game getGame(UUID id);
    void joinGame(UUID id, String name);
    void makeMove(UUID id, String name, Move move);
    GameDTO getGameView(UUID id);
}
