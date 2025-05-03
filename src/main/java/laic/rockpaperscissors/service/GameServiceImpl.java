package laic.rockpaperscissors.service;

import laic.rockpaperscissors.dto.GameDTO;
import laic.rockpaperscissors.exception.GameAlreadyFullException;
import laic.rockpaperscissors.exception.GameNotFoundException;
import laic.rockpaperscissors.exception.PlayerNotFoundException;
import laic.rockpaperscissors.model.Game;
import laic.rockpaperscissors.model.enums.GameStatus;
import laic.rockpaperscissors.model.enums.Move;
import laic.rockpaperscissors.model.Player;
import laic.rockpaperscissors.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service

public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public UUID createGame(String name) {
        var player1 = Player.builder()
                .name(name)
                .build();
        var game = Game.builder()
                .id(UUID.randomUUID())
                .player1(player1)
                .status(GameStatus.WAITING_FOR_PLAYER)
                .build();
        return gameRepository.save(game);
    }

    @Override
    public Game getGame(UUID id) {
        return gameRepository.findGame(id)
                .orElseThrow(() -> new GameNotFoundException("Game with id: " + id + " not found"));
    }

    @Override
    public GameDTO getGameView(UUID id) {
        Game game = getGame(id);

        String player1Name = game.getPlayer1() != null ? game.getPlayer1().getName() : null;
        String player2Name = game.getPlayer2() != null ? game.getPlayer2().getName() : null;

        String player1Move = game.getPlayer1() != null && game.getPlayer1().getMove() != null ?
                game.getPlayer1().getMove().name() : null;
        String player2Move = game.getPlayer2() != null && game.getPlayer2().getMove() != null ?
                game.getPlayer2().getMove().name() : null;

        return GameDTO.builder()
                .player1(player1Name)
                .player2(player2Name)
                .player1Move(player1Move)
                .player2Move(player2Move)
                .status(game.getStatus())
                .result(game.getResult())
                .build();
    }

    @Override
    public void joinGame(UUID id, String name) {
        var game = getGame(id);

        if (game.getStatus() == GameStatus.WAITING_FOR_MOVES) {
            throw new GameAlreadyFullException("Game with id: " + id + " is already full");
        }

        var player2 = Player.builder()
                .name(name)
                .build();

        game.setPlayer2(player2);
        game.setStatus(GameStatus.WAITING_FOR_MOVES);
        gameRepository.save(game);
    }

    @Override
    public void makeMove(UUID id, String name, Move move) {
        var game = getGame(id);

        if (game.getStatus().equals(GameStatus.WAITING_FOR_PLAYER)) {
            throw new IllegalStateException("You cannot make a move before player2 joining the game");
        }

        if (game.getPlayer1().getName().equals(name)) {
            game.getPlayer1().setMove(move);
        } else if (game.getPlayer2().getName().equals(name)) {
            game.getPlayer2().setMove(move);
        } else {
            throw new PlayerNotFoundException("Player " + name + " is not part of the game");
        }

        if (bothPlayersHaveMoves(game)) {
            resolveGame(game);
        }
    }

    private boolean bothPlayersHaveMoves(Game game) {
        return game.getPlayer1().getMove() != null && game.getPlayer2().getMove() != null;
    }

    private void resolveGame(Game game) {
        Move move1 = game.getPlayer1().getMove();
        Move move2 = game.getPlayer2().getMove();

        if (move1 == move2) {
            game.setResult("Draw");
        } else if (
                (move1 == Move.ROCK && move2 == Move.SCISSORS) ||
                        (move1 == Move.PAPER && move2 == Move.ROCK) ||
                        (move1 == Move.SCISSORS && move2 == Move.PAPER)
        ) {
            game.setResult(game.getPlayer1().getName() + " wins!");
        } else {
            game.setResult(game.getPlayer2().getName() + " wins!");
        }

        game.setStatus(GameStatus.FINISHED);
        gameRepository.save(game);
    }
}
