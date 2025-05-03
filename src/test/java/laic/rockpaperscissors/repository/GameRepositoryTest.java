package laic.rockpaperscissors.repository;

import laic.rockpaperscissors.model.Game;
import laic.rockpaperscissors.model.enums.GameStatus;
import laic.rockpaperscissors.model.Player;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameRepositoryTest {

    @Test
    void saveAndFindGame_shouldStoreAndReturnGame() {
        GameRepository repo = new GameRepository();

        var id = UUID.randomUUID();
        var game = Game.builder()
                .id(id)
                .status(GameStatus.WAITING_FOR_PLAYER)
                .player1(Player.builder().name("Alice").build())
                .build();

        repo.save(game);

        var found = repo.findGame(id);

        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getPlayer1().getName());
        assertEquals(GameStatus.WAITING_FOR_PLAYER, found.get().getStatus());
    }

    @Test
    void findGame_shouldReturnEmptyIfNotFound() {
        var repo = new GameRepository();
        var randomId = UUID.randomUUID();

        var result = repo.findGame(randomId);

        assertTrue(result.isEmpty());
    }
}