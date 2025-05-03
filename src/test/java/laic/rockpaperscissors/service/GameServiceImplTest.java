package laic.rockpaperscissors.service;

import laic.rockpaperscissors.exception.GameAlreadyFullException;
import laic.rockpaperscissors.exception.GameNotFoundException;
import laic.rockpaperscissors.exception.PlayerNotFoundException;
import laic.rockpaperscissors.model.Game;
import laic.rockpaperscissors.model.enums.GameStatus;
import laic.rockpaperscissors.model.enums.Move;
import laic.rockpaperscissors.model.Player;
import laic.rockpaperscissors.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameServiceImplTest {

    private GameRepository gameRepository;
    private GameServiceImpl gameService;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gameService = new GameServiceImpl(gameRepository);
    }

    @Test
    void shouldCreateGame() {
        ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> {
            Game savedGame = invocation.getArgument(0);
            return savedGame.getId();
        });

        var playerName = "Alice";
        var gameId = gameService.createGame(playerName);

        verify(gameRepository).save(gameCaptor.capture());
        var savedGame = gameCaptor.getValue();

        assertNotNull(savedGame.getId());
        assertEquals(playerName, savedGame.getPlayer1().getName());
        assertEquals(GameStatus.WAITING_FOR_PLAYER, savedGame.getStatus());
        assertEquals(savedGame.getId(), gameId);
    }

    @Test
    void shouldGetGameById() {
        var gameId = UUID.randomUUID();

        var game = Game.builder()
                .id(gameId)
                .build();

        when(gameRepository.findGame(gameId))
                .thenReturn(Optional.of(game));
    }

    @Test
    void shouldThrowIfGameNotFound() {
        var gameId = UUID.randomUUID();

        when(gameRepository.findGame(gameId)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> gameService.getGame(gameId));
    }

    @Test
    void shouldJoinGame() {
        var id = UUID.randomUUID();

        var game = Game.builder()
                .id(id)
                .status(GameStatus.WAITING_FOR_PLAYER)
                .player1(Player.builder().name("Alice").build())
                .build();

        when(gameRepository.findGame(id)).thenReturn(Optional.of(game));

        gameService.joinGame(id, "Bob");

        assertEquals(GameStatus.WAITING_FOR_MOVES, game.getStatus());
        assertEquals("Bob", game.getPlayer2().getName());
        verify(gameRepository).save(game);
    }

    @Test
    void shouldThrowIfGameAlreadyFull() {
        var id = UUID.randomUUID();

        var game = Game.builder()
                .id(id)
                .status(GameStatus.WAITING_FOR_MOVES)
                .build();

        when(gameRepository.findGame(id)).thenReturn(Optional.of(game));

        assertThrows(GameAlreadyFullException.class, () -> gameService.joinGame(id, "Charlie"));
    }

    @Test
    void shouldMakeMoveAndFinishGame() {
        var id = UUID.randomUUID();

        var player1 = Player.builder()
                .name("Alice")
                .build();
        var player2 = Player.builder()
                .name("Bob")
                .build();

        var game = Game.builder()
                .id(id)
                .player1(player1)
                .player2(player2)
                .status(GameStatus.WAITING_FOR_MOVES)
                .build();

        when(gameRepository.findGame(id)).thenReturn(Optional.of(game));

        gameService.makeMove(id, "Alice", Move.ROCK);
        gameService.makeMove(id, "Bob", Move.SCISSORS);

        assertEquals(GameStatus.FINISHED, game.getStatus());
        assertEquals("Alice wins!", game.getResult());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void shouldThrowIfMoveBeforeSecondPlayerJoins() {
        var id = UUID.randomUUID();

        var player1 = Player.builder()
                .name("Alice")
                .build();

        var game = Game.builder()
                .id(id)
                .player1(player1)
                .status(GameStatus.WAITING_FOR_PLAYER)
                .build();

        when(gameRepository.findGame(id)).thenReturn(Optional.of(game));

        assertThrows(IllegalStateException.class, () -> gameService.makeMove(id, "Alice", Move.ROCK));
    }

    @Test
    void shouldThrowIfUnknownPlayerMakesMove() {
        var id = UUID.randomUUID();

        var player1 = Player.builder()
                .name("Alice")
                .build();
        var player2 = Player.builder()
                .name("Bob")
                .build();

        var game = Game.builder()
                .id(id)
                .player1(player1)
                .player2(player2)
                .status(GameStatus.WAITING_FOR_MOVES)
                .build();

        when(gameRepository.findGame(id)).thenReturn(Optional.of(game));

        assertThrows(PlayerNotFoundException.class, () -> gameService.makeMove(id, "Eve", Move.PAPER));
    }
}